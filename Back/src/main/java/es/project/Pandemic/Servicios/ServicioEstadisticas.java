package es.project.Pandemic.Servicios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.CarpetaDePruebas;
import es.project.Pandemic.EntidadesYClasesSecundarias.Centro;
import es.project.Pandemic.EntidadesYClasesSecundarias.EstadisticaCentro;
import es.project.Pandemic.EntidadesYClasesSecundarias.EstadisticaCentroComunidad;
import es.project.Pandemic.EntidadesYClasesSecundarias.EstadisticaPruebaEnCentro;
import es.project.Pandemic.EntidadesYClasesSecundarias.PruebaComplementaria;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Repositorios.RepositorioCentros;
import es.project.Pandemic.Repositorios.RepositorioCitas;
import es.project.Pandemic.Repositorios.RepositorioPruebasComplementarias;
import es.project.Pandemic.Repositorios.RepositorioRoles;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;

@Service
public class ServicioEstadisticas {
	
	@Autowired
    private RepositorioRoles repositorioRoles;
	
	@Autowired
    private RepositorioUsuarios repositorioUsuarios;
	
	@Autowired
    private RepositorioCentros repositorioCentros;
	
	@Autowired
    private RepositorioCitas repositorioCitas;
	
	@Autowired
    private RepositorioPruebasComplementarias repositorioPruebasComplementarias;
	
	public List<EstadisticaCentro> getEstadisticasEnfermero(String documentoIdentidad, String fecha, boolean modoAnual) throws AbortException {
		String[] fechaFragmentada = fecha.split("-"); 
		if((!modoAnual && fechaFragmentada.length != 2) || (modoAnual && fechaFragmentada.length != 1)) throw new IllegalArgumentException("fecha");
		Integer mes = modoAnual ? null : Integer.parseInt(fechaFragmentada[0]);
		Integer anno = modoAnual ? Integer.parseInt(fechaFragmentada[0]) : Integer.parseInt(fechaFragmentada[1]);
		Usuario usuarioEnfermero = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		return getEstadisticasEnfermero(usuarioEnfermero, mes, anno, modoAnual);
	}
	
	public List<EstadisticaCentro> getEstadisticasEmpleado(String documentoIdentidad, Long idUsuarioEmpleado, String fecha, boolean modoAnual) throws AccountNotFoundException, AbortException, IllegalArgumentException {
		Usuario usuarioAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long centroDeAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(centroDeAdministrador == null || centroDeAdministrador < 0) throw new AbortException();
		String[] fechaFragmentada = fecha.split("-"); 
		if((!modoAnual && fechaFragmentada.length != 2) || (modoAnual && fechaFragmentada.length != 1)) throw new IllegalArgumentException("fecha");
		Integer mes = modoAnual ? null : Integer.parseInt(fechaFragmentada[0]);
		Integer anno = modoAnual ? Integer.parseInt(fechaFragmentada[0]) : Integer.parseInt(fechaFragmentada[1]);
		Usuario usuarioEnfermero = repositorioUsuarios.getReferenceById(idUsuarioEmpleado);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new IllegalArgumentException("idUsuario");
		return getEstadisticasEnfermeroEnCentro(usuarioEnfermero, centroDeAdministrador, mes, anno, modoAnual);
	}
	
	public List<EstadisticaPruebaEnCentro> getEstadisticasCentroDeAdmin(String documentoIdentidad, String fecha, boolean modoAnual, Long idCarpetaDePruebas)
	throws AccountNotFoundException, AbortException,IllegalArgumentException {
		Usuario usuarioAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long centroDeAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(centroDeAdministrador == null || centroDeAdministrador < 0) throw new AbortException();
		String[] fechaFragmentada = fecha.split("-"); 
		if((!modoAnual && fechaFragmentada.length != 2) || (modoAnual && fechaFragmentada.length != 1)) throw new IllegalArgumentException("fecha");
		Integer mes = modoAnual ? null : Integer.parseInt(fechaFragmentada[0]);
		Integer anno = modoAnual ? Integer.parseInt(fechaFragmentada[0]) : Integer.parseInt(fechaFragmentada[1]);
		return idCarpetaDePruebas != null
				? getEstadisticasPruebasDeCentro(centroDeAdministrador, idCarpetaDePruebas, mes, anno, modoAnual)
				: getEstadisticasCarpetasPruebasDeCentro(centroDeAdministrador, mes, anno, modoAnual);
	}
	
	public List<EstadisticaPruebaEnCentro> getEstadisticasCentro(Long idCentro, String fecha, boolean modoAnual, Long idCarpetaDePruebas) throws IllegalArgumentException {
		if(idCentro == null || idCentro < 0 || !repositorioCentros.existsById(idCentro)) throw new IllegalArgumentException("centro");
		String[] fechaFragmentada = fecha.split("-"); 
		if((!modoAnual && fechaFragmentada.length != 2) || (modoAnual && fechaFragmentada.length != 1)) throw new IllegalArgumentException("fecha");
		Integer mes = modoAnual ? null : Integer.parseInt(fechaFragmentada[0]);
		Integer anno = modoAnual ? Integer.parseInt(fechaFragmentada[0]) : Integer.parseInt(fechaFragmentada[1]);
		return idCarpetaDePruebas != null
				? getEstadisticasPruebasDeCentro(idCentro, idCarpetaDePruebas, mes, anno, modoAnual)
				: getEstadisticasCarpetasPruebasDeCentro(idCentro, mes, anno, modoAnual);
	}
	
	public List<EstadisticaCentroComunidad> getEstadisticasCentrosComunidad(String fecha, boolean modoAnual) throws IllegalArgumentException {
		String[] fechaFragmentada = fecha.split("-"); 
		if((!modoAnual && fechaFragmentada.length != 2) || (modoAnual && fechaFragmentada.length != 1)) throw new IllegalArgumentException("fecha");
		Integer mes = modoAnual ? null : Integer.parseInt(fechaFragmentada[0]);
		Integer anno = modoAnual ? Integer.parseInt(fechaFragmentada[0]) : Integer.parseInt(fechaFragmentada[1]);
		List<Centro> centrosComunidad = repositorioCentros.getTodosLosCentros();
		List<EstadisticaCentroComunidad> listadoEstadisticas = new ArrayList<>();
		for(Centro centro : centrosComunidad) {
			listadoEstadisticas.add(
					new EstadisticaCentroComunidad(
							centro.getId(), 
							centro.getNombre(), 
							getListadoTotalDeCitasDePeticion(mes, anno, modoAnual,
									modoAnual
										? repositorioCitas.getRecuentoCitasAnualDeCentro(centro.getId(), anno)
										: repositorioCitas.getRecuentoCitasMensualDeCentro(centro.getId(), mes, anno))
					)
			); 
		}
		return listadoEstadisticas;
	}
	
	public List<EstadisticaCentroComunidad> getEstadisticasComunidadPorCarpeta(Long idCarpeta, String fecha, boolean modoAnual) throws IllegalArgumentException {	
		if(idCarpeta == null || idCarpeta < 0 || repositorioPruebasComplementarias.existeCarpetaDePruebas(idCarpeta) <= 0) throw new IllegalArgumentException("idCarpeta");
		String[] fechaFragmentada = fecha.split("-"); 
		if((!modoAnual && fechaFragmentada.length != 2) || (modoAnual && fechaFragmentada.length != 1)) throw new IllegalArgumentException("fecha");
		Integer mes = modoAnual ? null : Integer.parseInt(fechaFragmentada[0]);
		Integer anno = modoAnual ? Integer.parseInt(fechaFragmentada[0]) : Integer.parseInt(fechaFragmentada[1]);
		List<Centro> centrosComunidad = repositorioCentros.getTodosLosCentros();
		List<EstadisticaCentroComunidad> listadoEstadisticas = new ArrayList<>();
		for(Centro centro : centrosComunidad) {
			try {	
				listadoEstadisticas.add(getEstadisticasCarpetaPruebasDeCentro(idCarpeta, centro.getId(), mes, anno, modoAnual)); 
			} catch(AbortException e) {}
		}
		return listadoEstadisticas;
	}
	
	public List<EstadisticaCentroComunidad> getEstadisticasComunidadPorPrueba(Long idPrueba, String fecha, boolean modoAnual) throws IllegalArgumentException {
		if(idPrueba == null || idPrueba < 0 || !repositorioPruebasComplementarias.existsById(idPrueba)) throw new IllegalArgumentException("idPrueba");
		String[] fechaFragmentada = fecha.split("-"); 
		if((!modoAnual && fechaFragmentada.length != 2) || (modoAnual && fechaFragmentada.length != 1)) throw new IllegalArgumentException("fecha");
		Integer mes = modoAnual ? null : Integer.parseInt(fechaFragmentada[0]);
		Integer anno = modoAnual ? Integer.parseInt(fechaFragmentada[0]) : Integer.parseInt(fechaFragmentada[1]);
		List<Centro> centrosComunidad = repositorioCentros.getTodosLosCentros();
		List<EstadisticaCentroComunidad> listadoEstadisticas = new ArrayList<>();
		for(Centro centro : centrosComunidad) {
			listadoEstadisticas.add(
					new EstadisticaCentroComunidad(
							centro.getId(), 
							centro.getNombre(), 
							getListadoTotalDeCitasDePeticion(mes, anno, modoAnual,
									modoAnual
										? repositorioCitas.getRecuentoCitasAnualDeCentroParaPrueba(centro.getId(), idPrueba, anno)
										: repositorioCitas.getRecuentoCitasMensualDeCentroParaPrueba(centro.getId(), idPrueba, mes, anno))
					)
			); 
		}
		return listadoEstadisticas;
	}
	
	private List<EstadisticaCentro> getEstadisticasEnfermero(Usuario usuarioEnfermero, Integer mes, Integer anno, boolean modoAnual) {
		List<Long> listadoCentros = repositorioRoles.getCentrosParaRolUsuario(usuarioEnfermero.getId(), Globales.ROLES.ENFERMERO);
		List<Long> listadoPruebasPuedeTratar = repositorioPruebasComplementarias.getPruebasDisponiblesParaUsuario(usuarioEnfermero.getId());
		List<EstadisticaCentro> listadoEstadisticas = new ArrayList<EstadisticaCentro>();
		for(Long idCentro : listadoCentros) {
			List<Long> listaTotalDeCitas = getListadoTotalDeCitasDePeticion(mes, anno, modoAnual, modoAnual
					? repositorioCitas.getRecuentoCitasAnualParaUsuarioEnCentro(anno, idCentro, usuarioEnfermero.getId())
					: repositorioCitas.getRecuentoCitasMensualParaUsuarioEnCentro(mes, anno, idCentro, usuarioEnfermero.getId()));
			if(listaSinCitas(listaTotalDeCitas)) continue;
			List<Long> totalDePruebasEnCentro = new ArrayList<>();
			for(Long idPrueba : listadoPruebasPuedeTratar) {
				totalDePruebasEnCentro.add(modoAnual
						? repositorioCitas.getRecuentoPruebaEnCentroAnualParaUsuario(idPrueba, idCentro, anno, usuarioEnfermero.getId())
						: repositorioCitas.getRecuentoPruebaEnCentroMensualParaUsuario(idPrueba, idCentro, mes, anno, usuarioEnfermero.getId())
				);
			}
			listadoEstadisticas.add(new EstadisticaCentro(repositorioCentros.getNombreDeCentro(idCentro), listaTotalDeCitas, listadoPruebasPuedeTratar, totalDePruebasEnCentro));
		}
		return listadoEstadisticas;
	}
	
	private List<EstadisticaCentro> getEstadisticasEnfermeroEnCentro(Usuario usuarioEnfermero, Long idCentro, Integer mes, Integer anno, boolean modoAnual) {
		List<Long> listadoPruebasPuedeTratar = repositorioPruebasComplementarias.getPruebasDisponiblesParaUsuario(usuarioEnfermero.getId());
		List<EstadisticaCentro> listadoEstadisticas = new ArrayList<EstadisticaCentro>();
		List<Long> listaTotalDeCitas = getListadoTotalDeCitasDePeticion(mes, anno, modoAnual, modoAnual
				? repositorioCitas.getRecuentoCitasAnualParaUsuarioEnCentro(anno, idCentro, usuarioEnfermero.getId())
				: repositorioCitas.getRecuentoCitasMensualParaUsuarioEnCentro(mes, anno, idCentro, usuarioEnfermero.getId()));
		if(listaSinCitas(listaTotalDeCitas)) return listadoEstadisticas;
		List<Long> totalDePruebasEnCentro = new ArrayList<>();
		for(Long idPrueba : listadoPruebasPuedeTratar) {
			totalDePruebasEnCentro.add(modoAnual
					? repositorioCitas.getRecuentoPruebaEnCentroAnualParaUsuario(idPrueba, idCentro, anno, usuarioEnfermero.getId())
					: repositorioCitas.getRecuentoPruebaEnCentroMensualParaUsuario(idPrueba, idCentro, mes, anno, usuarioEnfermero.getId())
			);
		}
		listadoEstadisticas.add(new EstadisticaCentro(repositorioCentros.getNombreDeCentro(idCentro), listaTotalDeCitas, listadoPruebasPuedeTratar, totalDePruebasEnCentro));
		return listadoEstadisticas;
	}
	
	/*Devuelve la estadistica de las carpetas de pruebas que puede tratar el centro*/
	private List<EstadisticaPruebaEnCentro> getEstadisticasCarpetasPruebasDeCentro(Long idCentro, Integer mes, Integer anno, boolean modoAnual) {
		List<CarpetaDePruebas> listadoCarpetasPuedeTratar = getCarpetasDePruebasTratablesDeCentro(idCentro);
		List<EstadisticaPruebaEnCentro> listadoEstadisticas = new ArrayList<EstadisticaPruebaEnCentro>();
		for(CarpetaDePruebas infoCarpeta : listadoCarpetasPuedeTratar) {
			List<PruebaComplementaria> listadoPruebasDeCarpeta = repositorioPruebasComplementarias.getTodasLasPruebasDentroDeCarpetaDePruebas(infoCarpeta.getId());
			List<Long> recuentoDeCitas = null;
			for(PruebaComplementaria infoPrueba : listadoPruebasDeCarpeta) {
				List<Long> listadoCitasDePrueba = getListadoTotalDeCitasDePeticion(mes, anno, modoAnual, modoAnual
						? repositorioCitas.getRecuentoCitasAnualParaPruebaEnCentro(anno, infoPrueba.getId(), idCentro)
						: repositorioCitas.getRecuentoCitasMensualParaPruebaEnCentro(mes, anno, infoPrueba.getId(), idCentro));
				if(recuentoDeCitas != null) {
					for(int indice = 0 ; indice < recuentoDeCitas.size() ; indice++) {
						recuentoDeCitas.set(indice, recuentoDeCitas.get(indice) + listadoCitasDePrueba.get(indice));
					}
				} else {
					recuentoDeCitas = listadoCitasDePrueba;
				}
			}
			listadoEstadisticas.add(
					new EstadisticaPruebaEnCentro(
							infoCarpeta.getId(),
							infoCarpeta.getNombre(),
							recuentoDeCitas
					)
			);
		}
		return listadoEstadisticas;
	}
	
	/*Devuelve la estadistica del centro con respecto a la carpeta seleccionada*/
	private EstadisticaCentroComunidad getEstadisticasCarpetaPruebasDeCentro(Long idCarpeta, Long idCentro, Integer mes, Integer anno, boolean modoAnual) throws AbortException {
		List<CarpetaDePruebas> listadoCarpetasPuedeTratar = getCarpetasDePruebasTratablesDeCentro(idCentro);
		if(!listadoCarpetasPuedeTratar.stream().anyMatch((carpeta) -> carpeta.getId() == idCarpeta)) throw new AbortException();
		List<PruebaComplementaria> listadoPruebasDeCarpeta = repositorioPruebasComplementarias.getTodasLasPruebasDentroDeCarpetaDePruebas(idCarpeta);
		List<Long> recuentoDeCitas = null;
		for(PruebaComplementaria infoPrueba : listadoPruebasDeCarpeta) {
			List<Long> listadoCitasDePrueba = getListadoTotalDeCitasDePeticion(mes, anno, modoAnual, modoAnual
					? repositorioCitas.getRecuentoCitasAnualParaPruebaEnCentro(anno, infoPrueba.getId(), idCentro)
					: repositorioCitas.getRecuentoCitasMensualParaPruebaEnCentro(mes, anno, infoPrueba.getId(), idCentro));
			if(recuentoDeCitas != null) {
				for(int indice = 0 ; indice < recuentoDeCitas.size() ; indice++) {
					recuentoDeCitas.set(indice, recuentoDeCitas.get(indice) + listadoCitasDePrueba.get(indice));
				}
			} else {
				recuentoDeCitas = listadoCitasDePrueba;
			}
		}
		return new EstadisticaCentroComunidad(
				idCentro,
				repositorioCentros.getNombreDeCentro(idCentro),
				recuentoDeCitas
		);
	}
	
	/*Devuelve la estadistica de las distintas pruebas de una carpeta de pruebas en el centro seleccionado*/
	private List<EstadisticaPruebaEnCentro> getEstadisticasPruebasDeCentro(Long idCentro, Long idCarpetaDePruebas, Integer mes, Integer anno, boolean modoAnual) {
		List<PruebaComplementaria> listadoPruebasDeCarpeta = repositorioPruebasComplementarias.getTodasLasPruebasDentroDeCarpetaDePruebas(idCarpetaDePruebas);
		List<EstadisticaPruebaEnCentro> listadoEstadisticas = new ArrayList<EstadisticaPruebaEnCentro>();
		for(PruebaComplementaria infoPrueba : listadoPruebasDeCarpeta) {
			listadoEstadisticas.add(
					new EstadisticaPruebaEnCentro(
							infoPrueba.getId(),
							infoPrueba.getNombre(),
							getListadoTotalDeCitasDePeticion(mes, anno, modoAnual, modoAnual
									? repositorioCitas.getRecuentoCitasAnualParaPruebaEnCentro(anno, infoPrueba.getId(), idCentro)
									: repositorioCitas.getRecuentoCitasMensualParaPruebaEnCentro(mes, anno, infoPrueba.getId(), idCentro))
					)
			);
		}
		return listadoEstadisticas;
	}
	
	private List<CarpetaDePruebas> getCarpetasDePruebasTratablesDeCentro(Long idCentro) {
		List<Long> listadoPruebasPuedeTratar = repositorioPruebasComplementarias.getPruebasTratablesEnCentro(idCentro);
		List<CarpetaDePruebas> listaDeCarpetas = new ArrayList<>();
		for(Long idPrueba : listadoPruebasPuedeTratar) {
			CarpetaDePruebas carpeta = new CarpetaDePruebas(repositorioPruebasComplementarias.getCarpetaDeLaPrueba(idPrueba).get(0));
			if(!listaDeCarpetas.stream().anyMatch(carpetaAñadida -> carpetaAñadida.getId() == carpeta.getId())) listaDeCarpetas.add(carpeta);
		}
		return listaDeCarpetas;
	}
	
	private List<Long> getListadoTotalDeCitasDePeticion(Integer mes, Integer anno, boolean modoAnual, List<Object[]> peticionDatos) {
		Map<Long, Long> mapaResultadosPeticion = new HashMap<Long, Long>();
		for(Object[] dato : peticionDatos) {
			mapaResultadosPeticion.put(((BigDecimal)dato[0]).longValue(), ((BigInteger)dato[1]).longValue());
		}
		List<Long> listadoFinal = new ArrayList<>();
		int longitudListadoFinal = modoAnual ? 12 : getDiasDeMes(mes, anno);
		for(Long indice = Long.valueOf(0); indice < longitudListadoFinal; indice++) {
			Long valorTotalCitas = mapaResultadosPeticion.get(indice + 1);
			listadoFinal.add(valorTotalCitas != null ? valorTotalCitas : 0);
		}
		return listadoFinal;
	}
	
	private int getDiasDeMes(int mes, int anno) {
		Calendar calendarioMes = new GregorianCalendar(anno, mes - 1, 1);
	    return calendarioMes.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	private boolean listaSinCitas(List<Long> listaCitas) {
		return !listaCitas.stream().anyMatch((Long cantidadCitas) -> cantidadCitas != 0);
	}
	
}
