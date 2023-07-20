package es.project.Pandemic.Servicios;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.AccountNotFoundException;

import org.aspectj.bridge.AbortException;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Centro;
import es.project.Pandemic.EntidadesYClasesSecundarias.CitaHistorico;
import es.project.Pandemic.EntidadesYClasesSecundarias.Horario;
import es.project.Pandemic.EntidadesYClasesSecundarias.HorarioPruebaCentro;
import es.project.Pandemic.EntidadesYClasesSecundarias.HorariosPrueba;
import es.project.Pandemic.EntidadesYClasesSecundarias.ImagenCentro;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.EntidadesYClasesSecundarias.Rol;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Repositorios.RepositorioCentros;
import es.project.Pandemic.Repositorios.RepositorioCitas;
import es.project.Pandemic.Repositorios.RepositorioPersonas;
import es.project.Pandemic.Repositorios.RepositorioPruebasComplementarias;
import es.project.Pandemic.Repositorios.RepositorioRoles;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;
@Service
public class ServicioCentros {
	
	@Autowired
    private RepositorioCentros repositorioCentros;
	
	@Autowired
    private RepositorioPruebasComplementarias repositorioPruebas;
	
	@Autowired
    private RepositorioUsuarios repositorioUsuarios;
	
	@Autowired
    private RepositorioPersonas repositorioPersonas;
	
	@Autowired
    private RepositorioRoles repositorioRoles;
	
	@Autowired
    private RepositorioCitas repositorioCitas;
	
	public List<Centro> getTodosLosCentros() {
		List<Centro> listadoCentros = repositorioCentros.getTodosLosCentros();
		listadoCentros.forEach(centro -> centro.setHorarios(this.getHorariosCentro(centro.getId())));
		return repositorioCentros.getTodosLosCentros();
	}
	
	public Centro getMiCentro(String documentoIdentidad) {
		Long idCentro = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidad).getCentroReferencia();
		Centro centro = repositorioCentros.getCentro(idCentro);
		centro.setHorarios(this.getHorariosCentro(idCentro));
		return centro;
	}
	
	public List<ImagenCentro> getImagenesDeMiCentro(String documentoIdentidad) {
		Long idCentro = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidad).getCentroReferencia();
		return this.getImagenesDeCentro(idCentro);
	}
	
	/*Devuelve la informacion del centro con los horarios de todas las pruebas que realice el centro*/
	public Centro getCentro(long idCentro) {
		Centro centro = repositorioCentros.getCentro(idCentro);
		centro.setHorarios(this.getHorariosCentro(idCentro));
		return centro;
	}
	
	/*Devuelve la informacion del centro con los horarios de una prueba puntual*/
	public Centro getCentro(long idCentro, long idPrueba) {
		Centro centro = repositorioCentros.getCentro(idCentro);
		centro.setHorarios(this.getHorariosCentro(idCentro, idPrueba));
		return centro;
	}
	
	/*Devuelve todos los centros que puedan realizar una prueba puntual*/
	public List<Centro> getCentrosConPrueba(long idPrueba) {
		List<Centro> listadoCentros = new ArrayList<Centro>();
		repositorioCentros.getCentroConPrueba(idPrueba).forEach((idCentro) -> listadoCentros.add(getCentro(idCentro, idPrueba)));
		return listadoCentros;
	}
	
	public List<String> getHorasDisponibles(long idCentro, long idPrueba, String cadenaFecha) throws ParseException {
		Date fecha = Globales.FECHAS.FORMATO_FECHA.parse(cadenaFecha);
		List<String> horasOcupadasPorCitas = repositorioCitas.getCitasFecha(idPrueba, idCentro, fecha).stream().map(hora -> hora.toString().substring(0, 5)).collect(Collectors.toList());
		List<String> horasCentro = getListaHorasCitas(idCentro, idPrueba, fecha);
		List<String> horasDisponibles = new ArrayList<>();
		for (String hora: horasCentro) {
			if(!horasOcupadasPorCitas.contains(hora)) {
				horasDisponibles.add(hora);
			}
		}
		return horasDisponibles;
	}
	
	public List<String> getDiasDisponibles(long idCentro, long idPrueba, YearMonth fechaAnnoMes) throws ParseException {
		DateTimeComparator comparador = DateTimeComparator.getDateOnlyInstance();
		List<String> diasDisponibles = new ArrayList<>();
		int totalDiasDelMes = fechaAnnoMes.lengthOfMonth(); 
		Date fechaActual = new Date();
		for (int dia = 1; dia <= totalDiasDelMes; dia++) {
			Calendar calendario = GregorianCalendar.getInstance();
			calendario.clear();
			calendario.set(fechaAnnoMes.getYear(), fechaAnnoMes.getMonthValue()-1, dia);
			Date fecha = calendario.getTime();
			int diaDeLaSemana = calendario.get(Calendar.DAY_OF_WEEK);
			
			/*Si la fecha es fin de semana o es anterior al dia actual no se añade como fecha disponible */
			if (comparador.compare(fechaActual, fecha) <= 0 && diaDeLaSemana != calendario.SATURDAY && diaDeLaSemana != calendario.SUNDAY) {				
				int totalPosiblesCitas = getListaHorasCitas(idCentro, idPrueba,fecha).size();
				List<Time> lista = repositorioCitas.getCitasFecha(idPrueba, idCentro, fecha);
				int totalCitasOcupadas = lista.size();
				
				/* No puede haber más de un cita en la misma hora en el mismo centro con la misma prueba,*/
				/* por lo que si hay menos citas ocupadas que las disponibles es que ese dias tiene alguna hora disponible*/
				if(totalCitasOcupadas < totalPosiblesCitas) {
					diasDisponibles.add(Globales.FECHAS.FORMATO_FECHA.format(fecha).toString());
				}
			}
		}
		
		return diasDisponibles;
	}
	
	private List<Horario> getHorarioCentroPrueba(long idCentro, long idPrueba) {
		List<HorarioPruebaCentro> horarioCentro = repositorioCentros.getHorarios(idCentro, idPrueba).stream().map(HorarioPruebaCentro::new).collect(Collectors.toList());
		List<Horario> listadoTodosHorariosPruebaEnCentro = new ArrayList<>();
		horarioCentro.forEach(horario -> listadoTodosHorariosPruebaEnCentro.add(new Horario(horario.getId(), horario.getHoraInicio(), horario.getHoraFin())));
		return listadoTodosHorariosPruebaEnCentro;
	}
	
	/*Devuelve los horarios del centro de todas las pruebas que haga*/
	private List<HorariosPrueba> getHorariosCentro(long idCentro) {
		List<Long> listadoIdsPruebas = repositorioCentros.getListadoPruebasDeCentro(idCentro);
		List<HorariosPrueba> horariosCentro = new ArrayList<>();
		for (Long idPrueba : listadoIdsPruebas) {
			List<HorarioPruebaCentro> listadoTodosHorariosPrueba = repositorioCentros.getHorarios(idCentro, idPrueba).stream().map(HorarioPruebaCentro::new).collect(Collectors.toList());
 			List<Horario> horariosAgrupados = new ArrayList<>();
			listadoTodosHorariosPrueba.forEach(horario -> horariosAgrupados.add(new Horario(horario.getId(), horario.getHoraInicio(), horario.getHoraFin())));
			horariosCentro.add(new HorariosPrueba(idPrueba, horariosAgrupados));
		}
		return horariosCentro;
	}
	
	/*Devuelve los horarios del centro de una prueba puntual*/
	private List<HorariosPrueba> getHorariosCentro(long idCentro, long idPrueba) {
		List<HorariosPrueba> horariosCentro = new ArrayList<>();
		List<HorarioPruebaCentro> listadoTodosHorariosPrueba = repositorioCentros.getHorarios(idCentro, idPrueba).stream().map(HorarioPruebaCentro::new).collect(Collectors.toList());
		List<Horario> horariosAgrupados = new ArrayList<>();
		listadoTodosHorariosPrueba.forEach(horario -> horariosAgrupados.add(new Horario(horario.getId(), horario.getHoraInicio(), horario.getHoraFin())));
		horariosCentro.add(new HorariosPrueba(idPrueba, horariosAgrupados));
		
		return horariosCentro;
	}
	
	private List<String> getListaHorasCitas(long idCentro, long idPrueba, Date fecha) {
		long duracionPrueba = repositorioPruebas.getPruebaComplementariaPorId(idPrueba).getDuracionPrueba();
		List<Horario> horariosCentro = getHorarioCentroPrueba(idCentro, idPrueba);
		List<String> listaHorasCitas = new ArrayList<>();
		for (Horario horario : horariosCentro) {
			LocalTime horaProximaCita = horario.getHoraInicio().toLocalTime();
			LocalTime horaTope = horario.getHoraFin().toLocalTime();
			while (horaProximaCita.isBefore(horaTope)) {
				listaHorasCitas.add(horaProximaCita.toString());
				horaProximaCita = horaProximaCita.plusMinutes(duracionPrueba);
			}
		}
		return listaHorasCitas;
	}
	
	public String getNombreCentroDeAdmin(String documentoIdentidad) throws AccessDeniedException, AbortException {
		Usuario usuarioAdmin = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdmin == null) throw new AccessDeniedException("");
		Long idCentroAdmin = repositorioRoles.getCentrosParaRolUsuario(usuarioAdmin.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(idCentroAdmin == null || idCentroAdmin < 0) throw new AbortException();
		return getCentro(idCentroAdmin).getNombre();
	}
	
	public List<HorarioPruebaCentro> getHorariosPruebaEnCentroDeAdmin(String documentoIdentidad, Long idPrueba) throws AccessDeniedException, AbortException, IllegalArgumentException {
		Usuario usuarioAdmin = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdmin == null) throw new AccessDeniedException("");
		Long idCentroAdmin = repositorioRoles.getCentrosParaRolUsuario(usuarioAdmin.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(idCentroAdmin == null || idCentroAdmin < 0) throw new AbortException();
		if(!repositorioPruebas.existsById(idPrueba)) throw new IllegalArgumentException();
		return repositorioCentros.getHorarios(idCentroAdmin, idPrueba).stream().map(HorarioPruebaCentro::new).collect(Collectors.toList());
	}
	
	public Pagina<List<HorariosPrueba>> getPruebasYHorariosCentroDeAdmin(String documentoIdentidad, int indicePaginaSolicitada, int numeroResultadosPagina) throws AccessDeniedException, AbortException {
		Usuario usuarioAdmin = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdmin == null) throw new AccessDeniedException("");
		Long idCentroAdmin = repositorioRoles.getCentrosParaRolUsuario(usuarioAdmin.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(idCentroAdmin == null || idCentroAdmin < 0) throw new AbortException();
		return this.getPaginaHorariosPruebas(getHorariosCentro(idCentroAdmin), indicePaginaSolicitada, numeroResultadosPagina);
	}
	
	public void eliminarPruebaCentroComoAdministrador(long idPruebaAElimininar, String documentoIdentidad) throws AccountNotFoundException, AbortException, IllegalArgumentException, InvalidAttributeIdentifierException{
		Usuario usuarioAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long centroDeAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(centroDeAdministrador == null || centroDeAdministrador < 0) throw new AbortException();
		if(!repositorioPruebas.existsById(idPruebaAElimininar)) throw new IllegalArgumentException();
		List<HorarioPruebaCentro> listaHorariosPruebasCentro = repositorioCentros.getHorarios(centroDeAdministrador, idPruebaAElimininar).stream().map(HorarioPruebaCentro::new).collect(Collectors.toList());
		for(HorarioPruebaCentro horarioPruebaCentro : listaHorariosPruebasCentro) {
			repositorioCentros.eliminarHorario(Long.valueOf(horarioPruebaCentro.getId()));
		}
	}
	
	public void annadirPruebaACentro (Map<String, String> formulario, String documentoIdentidad) throws AccountNotFoundException, AbortException, IllegalArgumentException, DataFormatException {
		Usuario usuarioAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long centroDeAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(centroDeAdministrador == null || centroDeAdministrador < 0) throw new AbortException();
		Long idPruebaAAnnadir = Long.valueOf(formulario.get("idPrueba"));
		if(!repositorioPruebas.existsById(idPruebaAAnnadir)) throw new IllegalArgumentException();
		Long proximaIdDisponible = repositorioCentros.getIdMasAltoHorarios() + 1;
		List<Horario> listaDeNuevosHorarios = new ArrayList<>();
		for(String cadenaHorario : Arrays.asList(formulario.get("horarios").split(";"))) {			
			String[] separacionCadenaHorarios = cadenaHorario.split("&");
			Time horaInicio = convertirCadenaHorarioEnTiempo(separacionCadenaHorarios[0]);
			Time horaFin = convertirCadenaHorarioEnTiempo(separacionCadenaHorarios[1]);
			listaDeNuevosHorarios.add(new Horario(proximaIdDisponible.intValue(), horaInicio, horaFin));
			proximaIdDisponible = proximaIdDisponible + 1;
		}
		for(Horario horario : listaDeNuevosHorarios) {
			if(horario.getHoraInicio() == null || horario.getHoraFin() == null) throw new DataFormatException();
			repositorioCentros.annadirNuevoHorarioCentroPrueba(horario.getId(), centroDeAdministrador, idPruebaAAnnadir, horario.getHoraInicio(), horario.getHoraFin());
		};
	}
	
	public void actualizarPruebaDeCentro (Map<String, String> formulario, String documentoIdentidad) throws AccountNotFoundException, AbortException, IllegalArgumentException, DataFormatException {
		Usuario usuarioAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long centroDeAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(centroDeAdministrador == null || centroDeAdministrador < 0) throw new AbortException();
		Long idPruebaAActualizar = Long.valueOf(formulario.get("idPrueba"));
		if(!repositorioPruebas.existsById(idPruebaAActualizar)) throw new IllegalArgumentException();
		List<Horario> listaDeHorariosActuales = getHorarioCentroPrueba(centroDeAdministrador, idPruebaAActualizar);
		Long proximaIdDisponible = repositorioCentros.getIdMasAltoHorarios() + 1;
		List<Horario> listaDeNuevosHorarios = new ArrayList<>();
		for(String cadenaHorario : Arrays.asList(formulario.get("horarios").split(";"))) {			
			String[] separacionCadenaHorarios = cadenaHorario.split("&");
			Time horaInicio = convertirCadenaHorarioEnTiempo(separacionCadenaHorarios[0]);
			Time horaFin = convertirCadenaHorarioEnTiempo(separacionCadenaHorarios[1]);
			Horario horarioExistente = getHorarioSiYaExiste(horaInicio, horaFin, listaDeHorariosActuales);
			if(horarioExistente != null) {
				listaDeHorariosActuales.remove(horarioExistente);
				continue;
			}
			listaDeNuevosHorarios.add(new Horario(proximaIdDisponible.intValue(), horaInicio, horaFin));
			proximaIdDisponible = proximaIdDisponible + 1;
		}
		for(Horario horario : listaDeHorariosActuales) {
			repositorioCentros.eliminarHorario(horario.getId());
		}
		for(Horario horario : listaDeNuevosHorarios) {
			if(horario.getHoraInicio() == null || horario.getHoraFin() == null) throw new DataFormatException();
			repositorioCentros.annadirNuevoHorarioCentroPrueba(horario.getId(), centroDeAdministrador, idPruebaAActualizar, horario.getHoraInicio(), horario.getHoraFin());
		};
	}
	
	public void eliminarCentroComoAdministrador(Long idCentro) {
		repositorioCentros.deleteById(idCentro);
	}
	
	public void annadirNuevoCentroAComunidad(Map<String, String> formularioDatosCentro) {
		Centro centro = repositorioCentros.saveAndFlush(new Centro(repositorioCentros.getIdMasAltoCentro() + 1, formularioDatosCentro.get("nombre"), formularioDatosCentro.get("direccion"),
				formularioDatosCentro.get("codigoPostal"), formularioDatosCentro.get("municipio"), formularioDatosCentro.get("cartaPresentacion"),
				formularioDatosCentro.get("historia"), formularioDatosCentro.get("mision")));
		List<Long> idsUsuariosAdministradores = Arrays.asList(formularioDatosCentro.get("listadoAdministradores").split(",")).stream().map(cadenaId -> Long.valueOf(cadenaId)).collect(Collectors.toList());
		for(Long idUsuarioAdministrador : idsUsuariosAdministradores) {
			convertirEnAdministradorDeHospitalAUsuario(idUsuarioAdministrador, centro.getId());
		}
	}
	
	public void actualizarInformacionCentro(Long idCentro, Map<String, String> formularioDatosCentro) throws IllegalArgumentException {
		if(!repositorioCentros.existsById(idCentro)) throw new IllegalArgumentException(idCentro.toString()); 
		Centro centro = repositorioCentros.saveAndFlush(new Centro(idCentro, formularioDatosCentro.get("nombre"), formularioDatosCentro.get("direccion"),
				formularioDatosCentro.get("codigoPostal"), formularioDatosCentro.get("municipio"), formularioDatosCentro.get("cartaPresentacion"),
				formularioDatosCentro.get("historia"), formularioDatosCentro.get("mision")));
		List<Long> idsUsuariosAdministradores = Arrays.asList(formularioDatosCentro.get("listadoAdministradores").split(",")).stream().map(cadenaId -> Long.valueOf(cadenaId)).collect(Collectors.toList());
		for(Long idUsuarioAdministrador : idsUsuariosAdministradores) {
			convertirEnAdministradorDeHospitalAUsuario(idUsuarioAdministrador, centro.getId());
		}
	}
	
	public List<ImagenCentro> getImagenesDeCentro(Long idCentro) {
		Centro centro = repositorioCentros.getCentro(idCentro);
		if(centro == null) throw new IllegalArgumentException();
		List<ImagenCentro> listaImagenesDeCentro = repositorioCentros.getImagenesDeCentro(idCentro).stream().map(ImagenCentro::new).collect(Collectors.toList());
		//Descomprimir los datos antes de enviar las imagenes
		listaImagenesDeCentro.forEach(imagen -> imagen.descomprimirDatos());
		return listaImagenesDeCentro != null ? listaImagenesDeCentro : new ArrayList<>();
	}
	
	public void actualizarImagenesDeCentro(Long idCentro, List<MultipartFile> nuevasImagenesCentro) throws IllegalArgumentException, IOException {
		Centro centro = repositorioCentros.getCentro(idCentro);
		if(centro == null) throw new IllegalArgumentException();
		List<ImagenCentro> listadoImagenesGuardadasDeCentro = repositorioCentros.getImagenesCentro(idCentro).stream().map(ImagenCentro::new).collect(Collectors.toList());
		for(ImagenCentro imagenGuardada : listadoImagenesGuardadasDeCentro) {
			int indiceImagen = nuevasImagenesContienenImagenGuardada(imagenGuardada, nuevasImagenesCentro);
			if(indiceImagen > -1) {
				//Si la imagen ya guardada esta dentro de la lista de nuevas imagenes del centro la quitamos de la lista de nuevas imagenes que añadir en la base de datos
				nuevasImagenesCentro.remove(indiceImagen);
			} else {				
				//Si la imagen ya guardada no esta dentro de la lista de nuevas imagenes del centro se elimina de la base de datos
				repositorioCentros.eliminarImagenDeCentro(imagenGuardada.getId());
			} 
		}
		repositorioCentros.flush();
		//Se añaden las nuevas imagenes restantes a la base de datos
		for(MultipartFile datosImagenNueva : nuevasImagenesCentro) {
			Optional<Long> ultimoIdOcupado = repositorioCentros.getIdMasAltoImagenCentro();
			//Comprimir los datos de la imagen antes de guardarla en base de datos
			repositorioCentros.annadirImagenACentro(ultimoIdOcupado.isPresent() ? ultimoIdOcupado.get() + 1 : 1, idCentro, datosImagenNueva.getContentType(), datosImagenNueva.getName(), ImagenCentro.comprimirImagen(datosImagenNueva.getBytes()));
			repositorioCentros.flush();
		}
	}
	
	private Horario getHorarioSiYaExiste(Time horaInicioAComprobar,Time horaFinAComprobar,List<Horario> listadoDeHorariosExistentes) {
		for(Horario horarioExistente : listadoDeHorariosExistentes) {
			if(horarioExistente.getHoraInicio().getTime() == horaInicioAComprobar.getTime() && horarioExistente.getHoraFin().getTime() == horaFinAComprobar.getTime()) return horarioExistente;
		}
		return null;
	}
	
	private Time convertirCadenaHorarioEnTiempo(String cadenaHoario) {
		try {
			return new java.sql.Time(Globales.FECHAS.FORMATO_HORA.parse(cadenaHoario).getTime());
		} catch (ParseException e) {
			return null;
		}
	}
	
	private Pagina<List<HorariosPrueba>> getPaginaHorariosPruebas(List<HorariosPrueba> listadoHorariosPruebasTotales, int indicePaginaSolicitada, int numeroResultadosPagina) {
		if (listadoHorariosPruebasTotales.size() > 0 && listadoHorariosPruebasTotales.size() <= (numeroResultadosPagina * indicePaginaSolicitada)) throw new ArrayIndexOutOfBoundsException();
		List<HorariosPrueba> listadoResultado = listadoHorariosPruebasTotales.subList(
				numeroResultadosPagina * indicePaginaSolicitada,
				numeroResultadosPagina * (indicePaginaSolicitada + 1) < listadoHorariosPruebasTotales.size()
					? numeroResultadosPagina * (indicePaginaSolicitada > 0 ? indicePaginaSolicitada + 1 : 1)
					: listadoHorariosPruebasTotales.size());
		Pagina<List<HorariosPrueba>> pagina = new Pagina(listadoResultado, indicePaginaSolicitada, numeroResultadosPagina, listadoHorariosPruebasTotales.size());
		return pagina;
	}
	
	private void convertirEnAdministradorDeHospitalAUsuario(Long idUsuario,Long idCentro) {
		if(repositorioRoles.tieneUsuarioRolEnCentro(idUsuario, Globales.ROLES.ADMINISTRADOR_HOSPITAL, idCentro) == 0) {
			for(Rol rol : repositorioRoles.getRolesUsuarioSinCentro(idUsuario)) {
				repositorioRoles.eliminarPermiso(rol.getId());
			};
			repositorioRoles.asignarPermiso(repositorioRoles.getIdMasAltoPermisos() + 1, idUsuario, Globales.ROLES.ADMINISTRADOR_HOSPITAL, idCentro);
		}
	}
	
	private int nuevasImagenesContienenImagenGuardada(ImagenCentro imagenGuardada, List<MultipartFile> datosNuevasImagenes) throws IOException {
		for(int indice = 0; indice < datosNuevasImagenes.size(); indice ++) {
			MultipartFile nuevaImagen = datosNuevasImagenes.get(indice);
			if(nuevaImagen.getName().equals(imagenGuardada.getNombre()) && nuevaImagen.getContentType().equals(imagenGuardada.getTipo())) return indice;
		}
		return -1;
	}
}
