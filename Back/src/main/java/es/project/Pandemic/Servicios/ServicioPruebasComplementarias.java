package es.project.Pandemic.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import javax.persistence.EntityNotFoundException;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.CarpetaDePruebas;
import es.project.Pandemic.EntidadesYClasesSecundarias.Cita;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.EntidadesYClasesSecundarias.Persona;
import es.project.Pandemic.EntidadesYClasesSecundarias.PruebaComplementaria;
import es.project.Pandemic.EntidadesYClasesSecundarias.RequisitosPrueba;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Repositorios.RepositorioCitas;
import es.project.Pandemic.Repositorios.RepositorioPersonas;
import es.project.Pandemic.Repositorios.RepositorioPruebasComplementarias;
import es.project.Pandemic.Repositorios.RepositorioRelacionPruebasPacientes;
import es.project.Pandemic.Repositorios.RepositorioRoles;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;

@Service
public class ServicioPruebasComplementarias {
	@Autowired
    private RepositorioPruebasComplementarias repositorioPruebas;
	
	@Autowired
    private RepositorioRelacionPruebasPacientes repositorioRelacionesPruebasPacientes;
	
	@Autowired
    private RepositorioPersonas repositorioPersonas;
	
	@Autowired
    private RepositorioUsuarios repositorioUsuarios;
	
	@Autowired
    private RepositorioRoles repositorioRoles;
	
	@Autowired
    private RepositorioCitas repositorioCitas;
	
	public PruebaComplementaria getPruebaComplemetariaPorId(long idPrueba) {
		return repositorioPruebas.getPruebaComplementariaPorId(idPrueba);
	}
	
	public List<PruebaComplementaria> getTodasPruebasComplementarias() {
		return repositorioPruebas.getTodasPruebasComplementarias();
	}
	
	public List<CarpetaDePruebas> getTodasCarpetasPruebas() {
		return repositorioPruebas.getTodasCarpetasDePruebas().stream().map(CarpetaDePruebas::new).collect(Collectors.toList());
	}
	
	public List<PruebaComplementaria> getTodasPruebasPaciente(String idPaciente) {
		Persona paciente = repositorioPersonas.getPersonaPorDocumentoIdentidad(idPaciente);
		List<PruebaComplementaria> listadoPruebas = new ArrayList<>();
		for(Long idPrueba : repositorioRelacionesPruebasPacientes.getTodasPruebasPaciente(paciente.getId())) {
			listadoPruebas.add(repositorioPruebas.getPruebaComplementariaPorId(idPrueba));
		} 
		return listadoPruebas;
	}
	
	public List<JsonNode> getPruebasIncompletasSinCitaPaciente(String idPaciente) { /*Cambiar para mostrar solo aquellas pruebas incompletas que sean las siguiente por carpeta*/
		Persona paciente = repositorioPersonas.getPersonaPorDocumentoIdentidad(idPaciente);
		List<JsonNode> listadoPruebas = new ArrayList<>();
		List<Long> pruebasIncompletasSinCita = repositorioRelacionesPruebasPacientes.getPruebasIncompletasPaciente(paciente.getId()).stream().filter((Long idPrueba) -> {
			return repositorioCitas.existeCitaPendiente(idPrueba, paciente.getId()) == 0;
		}).toList();
		for(Long idPrueba : pruebasIncompletasSinCita) {
			ObjectMapper mapeador = new ObjectMapper();
			ObjectNode infoPrueba = mapeador.convertValue(repositorioPruebas.getPruebaComplementariaPorId(idPrueba), ObjectNode.class);
			PruebaComplementaria prueba = repositorioPruebas.getPruebaComplementariaPorId(idPrueba);
			List<RequisitosPrueba> listaRequisitosPrueba = repositorioPruebas.getRequisitosPrueba(idPrueba).stream().map(RequisitosPrueba::new).filter(requisito -> requisito.isMuyRecomendable()).collect(Collectors.toList());			
			if(listaRequisitosPrueba.stream().anyMatch(requisito -> requisito.cumpleRequisitoPaciente(paciente.getEdad()))) {
				infoPrueba.put("muyRecomendable", true);
			} else {
				infoPrueba.put("muyRecomendable", false);
			}
			
			listadoPruebas.add(mapeador.convertValue(infoPrueba, JsonNode.class));
		}
		return listadoPruebas;
	}
	
	public List<String> getPruebasPendientes(long idPaciente) {
		Persona paciente = repositorioPersonas.getPersona(idPaciente);
		List<Cita> listadoCitasPendientes = repositorioCitas.getCitasPendientesPaciente(idPaciente).stream().map(Cita::new).collect(Collectors.toList());
		List<Long> listadoPruebas = new ArrayList<>();
		for(Cita cita : listadoCitasPendientes) {
			if(!listadoPruebas.contains(cita.getPrueba())) listadoPruebas.add(cita.getPrueba());
		}
		return listadoPruebas.stream().map((idPrueba) -> repositorioPruebas.getNombrePruebaComplementaria(idPrueba)).collect(Collectors.toList());
	}
	
	public List<String> getPruebasRealizadas(long idPaciente) {
		return repositorioRelacionesPruebasPacientes.getPruebasRealizadas(idPaciente).stream().map(idPrueba -> repositorioPruebas.getNombrePruebaComplementaria(idPrueba)).collect(Collectors.toList());
	}
	
	public List<String> getPruebasTratablesPorEmpleado(long idEmpleado) {
		return repositorioPruebas.getPruebasDisponiblesParaUsuario(idEmpleado).stream().map(idPrueba -> repositorioPruebas.getNombrePruebaComplementaria(idPrueba)).collect(Collectors.toList());
	}
	
	public List<Long> getIdsPruebasTratablesPorEmpleado(long idEmpleado) {
		return repositorioPruebas.getPruebasDisponiblesParaUsuario(idEmpleado);
	}
	
	public List<Long> getPruebasQuePuedeTratarCentroDeAdmin(String documentoIdentidad) throws AccessDeniedException, AbortException {
		Usuario usuarioAdmin = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdmin == null) throw new AccessDeniedException("");
		Long idCentroAdmin = repositorioRoles.getCentrosParaRolUsuario(usuarioAdmin.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(idCentroAdmin == null || idCentroAdmin < 0) throw new AbortException();
		return repositorioPruebas.getPruebasTratablesEnCentro(idCentroAdmin);
	}
	
	public boolean existePrueba(long idPrueba) {
		return repositorioPruebas.existsById(idPrueba);
	}
	
	public void annadirNuevaPruebaAPaciente(Long idPrueba, Long idPaciente) {
		Long id = repositorioRelacionesPruebasPacientes.getIdMasAltoRelacionPruebasPacientes() + 1;
		repositorioRelacionesPruebasPacientes.annadirNuevaPruebaAPaciente(id, idPrueba, idPaciente);
	}
	
	public void asignarNuevaPruebaAEnfermero(Long id, Long idPrueba, Long usuarioEnfermero) {
		repositorioPruebas.asignarNuevaPruebaAEnfermero(id, usuarioEnfermero, idPrueba);
	}
	
	public void desasignarPruebaAEnfermero(Long idPrueba, Long usuarioEnfermero) {
		repositorioPruebas.desasignarPruebaAEnfermero(idPrueba, usuarioEnfermero);
	}
	
	public Long getIdMasAltoEnfermeroPruebas() {
		return repositorioPruebas.getIdMasAltoEnfermeroPruebas();
	}
	
	public boolean puedeTratarEnfermeroPrueba(Long idPrueba, Long usuarioEnfermero) {
		return repositorioPruebas.puedeTratarEnfermeroPrueba(idPrueba, usuarioEnfermero) > 0;
	}
	
	public List<PruebaComplementaria> getPruebasTratablesEnMiCentro(String documentoIdentidad) {
		Long centroUsuario = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidad).getCentroReferencia();
		List<PruebaComplementaria> listadoPruebasDisponibles = new ArrayList<>();
		for(Long idPrueba : repositorioPruebas.getPruebasTratablesEnCentro(centroUsuario)) {
			listadoPruebasDisponibles.add(repositorioPruebas.getPruebaComplementariaPorId(idPrueba));
		}
		return listadoPruebasDisponibles;
	}
	
	public List<Long> getPruebasTratablesEnCentro(Long idCentro) {
		return repositorioPruebas.getPruebasTratablesEnCentro(idCentro);
	}
	
	public void crearNuevaCarpetaDePruebas(Map<String, String> formulario) throws IllegalArgumentException {
		if(formulario.get("nombre") == null) throw new IllegalArgumentException("nombre");
		Long idMasAltoDeCarpetas = repositorioPruebas.getIdMasAltoCarpetasDePruebas();
		repositorioPruebas.crearNuevaCarpetaDePruebas(idMasAltoDeCarpetas + 1, formulario.get("nombre"));
	}
	
	public void crearNuevaPrueba(Long idCarpeta, JsonNode formulario) throws IllegalArgumentException, DataFormatException {
		String nombre = formulario.get("nombre").asText();
		if(nombre == null || repositorioPruebas.getNumeroDePruebasConNombre(nombre) > 0) throw new IllegalArgumentException("nombre");
		long duracion = formulario.get("duracion").asLong();
		if(duracion < 0) throw new IllegalArgumentException("duracion");
		long separacionSiguientePrueba = formulario.get("separacionSiguientePrueba").asLong();
		if(separacionSiguientePrueba < 0) throw new IllegalArgumentException("separacionSiguientePrueba");
		Long idPrueba = repositorioPruebas.getIdMasAltoPruebas() + 1;
		String descripcion = formulario.get("descripcion").asText();
		if(descripcion == null || descripcion.equals("")) throw new IllegalArgumentException("descripcion");
		String avisos = formulario.get("avisos").asText();
		if(avisos == null || avisos.equals("")) throw new IllegalArgumentException("avisos");
		String recomendaciones = formulario.get("recomendaciones").asText();
		if(recomendaciones == null || recomendaciones.equals("")) throw new IllegalArgumentException("recomendaciones");
		repositorioPruebas.crearNuevaPrueba(idPrueba , nombre, duracion, separacionSiguientePrueba, descripcion, avisos, recomendaciones);
		this.annadirPruebaACarpetaDePruebas(idCarpeta, idPrueba);
		List<JsonNode> nuevosRequisitos = new ArrayList<>();
		for (JsonNode rango : formulario.get("rangosEdad")) {
			nuevosRequisitos.add(rango);
		}
		Long idMasAltaRequisitos = repositorioPruebas.getIdMasAltoRequisitosPruebas();
		Long proximaIdRequisitosDisponible = idMasAltaRequisitos != null ? idMasAltaRequisitos + 1 : 1;
		List<RequisitosPrueba> listaNuevoRequisitos = new ArrayList<>();
		for(int indice = 0; indice < nuevosRequisitos.size(); indice++) {
			JsonNode formularioRequisito = nuevosRequisitos.get(indice);
			RequisitosPrueba requisito = new RequisitosPrueba();
			requisito.setId(proximaIdRequisitosDisponible);
			if(requisito.getId() < 0) throw new DataFormatException("id"+";"+indice);
			requisito.setPrueba(idPrueba);			
			requisito.setMuyRecomendable(formularioRequisito.get("muyRecomendable").asBoolean());
			System.out.println(formularioRequisito.get("edadInicio"));
			requisito.setEdadInicio(formularioRequisito.get("edadInicio").isNull() ? null : formularioRequisito.get("edadInicio").asLong());
			if(requisito.getEdadInicio() != null &&  requisito.getEdadInicio() < 0) throw new DataFormatException("edadInicio"+";"+indice);
			requisito.setEdadFin(formularioRequisito.get("edadFin").isNull() ? null : formularioRequisito.get("edadFin").asLong());
			if(requisito.getEdadFin() != null &&  requisito.getEdadFin() < 0) throw new DataFormatException("edadFin"+";"+indice);
			listaNuevoRequisitos.add(requisito);
			proximaIdRequisitosDisponible = proximaIdRequisitosDisponible + 1;
		}
		for(RequisitosPrueba requisito : listaNuevoRequisitos) {
			repositorioPruebas.annadirRequisitoAPrueba(requisito.getId(), requisito.getPrueba(), requisito.isMuyRecomendable(), requisito.getEdadInicio(), requisito.getEdadFin());
		};
	}
	
	public void actualizarCarpetaDePruebas(Map<String, String> formulario) throws IllegalArgumentException, EntityNotFoundException {
		Long idCarpeta = Long.valueOf(formulario.get("id"));
		String nombreCarpeta = formulario.get("nombre");
		if(idCarpeta == null) throw new IllegalArgumentException("id");
		if(nombreCarpeta == null) throw new IllegalArgumentException("nombre");
		if(repositorioPruebas.existeCarpetaDePruebas(idCarpeta) == 0) throw new EntityNotFoundException();
		repositorioPruebas.actualizarCarpetaDePruebas(idCarpeta, formulario.get("nombre"));
	}
	
	public void actualizarPrueba(JsonNode formulario) throws IllegalArgumentException, EntityNotFoundException, DataFormatException {
		Long idPrueba = formulario.get("id").asLong();
		String nombre = formulario.get("nombre").asText();
		if(nombre == null) throw new IllegalArgumentException("nombre");
		PruebaComplementaria datosPrueba = repositorioPruebas.getPruebaPorNombre(nombre);
		if(datosPrueba != null && datosPrueba.getId() != idPrueba) throw new IllegalArgumentException("nombre");
		long duracion = formulario.get("duracion").asLong();
		if(duracion < 0) throw new IllegalArgumentException("duracion");
		long separacionSiguientePrueba = formulario.get("separacionSiguientePrueba").asLong();
		if(separacionSiguientePrueba < 0) throw new IllegalArgumentException("separacionSiguientePrueba");
		if(!repositorioPruebas.existsById(idPrueba)) throw new EntityNotFoundException();
		String descripcion = formulario.get("descripcion").asText();
		if(descripcion == null || descripcion.equals("")) throw new IllegalArgumentException("descripcion");
		String avisos = formulario.get("avisos").asText();
		if(avisos == null || avisos.equals("")) throw new IllegalArgumentException("avisos");
		String recomendaciones = formulario.get("recomendaciones").asText();
		if(recomendaciones == null || recomendaciones.equals("")) throw new IllegalArgumentException("recomendaciones");
		repositorioPruebas.actualizarPrueba(idPrueba , nombre, duracion, separacionSiguientePrueba, descripcion, avisos, recomendaciones);
		List<JsonNode> nuevosRequisitos = new ArrayList<>();
		for (JsonNode rango : formulario.get("rangosEdad")) {
			nuevosRequisitos.add(rango);
		}
		Long idMasAltaRequisitos = repositorioPruebas.getIdMasAltoRequisitosPruebas();
		Long proximaIdRequisitosDisponible = idMasAltaRequisitos != null ? idMasAltaRequisitos + 1 : 1;
		List<RequisitosPrueba> listaDeRequisitosActuales = this.getRequisitosDePrueba(idPrueba);
		List<RequisitosPrueba> listaNuevoRequisitos = new ArrayList<>();
		for(int indice = 0; indice < nuevosRequisitos.size(); indice++) {
			JsonNode formularioRequisito = nuevosRequisitos.get(indice);
			RequisitosPrueba requisito = new RequisitosPrueba();
			requisito.setId(proximaIdRequisitosDisponible);
			if(requisito.getId() < 0) throw new DataFormatException("id"+";"+indice);
			requisito.setPrueba(idPrueba);			
			requisito.setMuyRecomendable(formularioRequisito.get("muyRecomendable").asBoolean());
			requisito.setEdadInicio(formularioRequisito.get("edadInicio").isNull() ? null : formularioRequisito.get("edadInicio").asLong());
			if(requisito.getEdadInicio() != null &&  requisito.getEdadInicio() < 0) throw new DataFormatException("edadInicio"+";"+indice);
			requisito.setEdadFin(formularioRequisito.get("edadFin").isNull() ? null : formularioRequisito.get("edadFin").asLong());
			if(requisito.getEdadFin() != null &&  requisito.getEdadFin() < 0) throw new DataFormatException("edadFin"+";"+indice);
			RequisitosPrueba requisitoExistente = getRequisitoSiYaExiste(requisito, listaDeRequisitosActuales);
			if(requisitoExistente != null) {
				listaDeRequisitosActuales.remove(requisitoExistente);
				continue;
			}
			listaNuevoRequisitos.add(requisito);
			proximaIdRequisitosDisponible = proximaIdRequisitosDisponible + 1;
		}
		for(RequisitosPrueba requisito : listaDeRequisitosActuales) {
			repositorioPruebas.eliminarRequisito(requisito.getId());
		}
		for(RequisitosPrueba requisito : listaNuevoRequisitos) {
			repositorioPruebas.annadirRequisitoAPrueba(requisito.getId(), requisito.getPrueba(), requisito.isMuyRecomendable(), requisito.getEdadInicio(), requisito.getEdadFin());
		};
	}
	
	public void eliminarCarpetaDePruebas(Long idCarpetaDePruebas) throws IllegalArgumentException, EntityNotFoundException {
		if(idCarpetaDePruebas == null) throw new IllegalArgumentException("id");
		if(repositorioPruebas.existeCarpetaDePruebas(idCarpetaDePruebas) == 0) throw new EntityNotFoundException();
		for (PruebaComplementaria prueba: repositorioPruebas.getTodasLasPruebasDentroDeCarpetaDePruebas(idCarpetaDePruebas)) {
			repositorioPruebas.eliminarPrueba(prueba.getId());
		}
		repositorioPruebas.eliminarCarpetaDePruebas(idCarpetaDePruebas);
	}
	
	public void eliminarPrueba(Long idPrueba) throws IllegalArgumentException, EntityNotFoundException {
		if(idPrueba == null) throw new IllegalArgumentException("id");
		if(!repositorioPruebas.existsById(idPrueba)) throw new EntityNotFoundException();
		repositorioPruebas.eliminarPrueba(idPrueba);
	}
	
	public List<RequisitosPrueba> getRequisitosDePrueba(long idPrueba) throws IllegalArgumentException{
		if(!repositorioPruebas.existsById(idPrueba)) throw new IllegalArgumentException("idPrueba");
		return repositorioPruebas.getRequisitosPrueba(idPrueba).stream().map(RequisitosPrueba::new).collect(Collectors.toList());
	}
	
	public List<PruebaComplementaria> getTodasLasPruebasDentroDeCarpetaDePruebas(Long idCarpeta) {
		return repositorioPruebas.getTodasLasPruebasDentroDeCarpetaDePruebas(idCarpeta); 
	}
	
	public Pagina<List<PruebaComplementaria>> getListadoPruebasDeCarpetaDePruebas(String documentoIdentidadPaciente, int indicePaginaSolicitada, int numeroResultadosPagina, Long idCarpetadePruebas) throws IllegalArgumentException {
		if(repositorioPruebas.existeCarpetaDePruebas(idCarpetadePruebas) == 0) throw new IllegalArgumentException();
		
		List<PruebaComplementaria> listadoTodasPruebasDeCarpetaDePruebas = repositorioPruebas.getTodasLasPruebasDentroDeCarpetaDePruebas(idCarpetadePruebas);
		return this.getPaginaPruebas(listadoTodasPruebasDeCarpetaDePruebas, indicePaginaSolicitada, numeroResultadosPagina);
	}
	
	public Pagina<List<CarpetaDePruebas>> getListadoCarpetasDePruebas(String documentoIdentidadPaciente, int indicePaginaSolicitada, int numeroResultadosPagina) {
		List<CarpetaDePruebas> listadoTodasCarpetasDePruebas = repositorioPruebas.getTodasCarpetasDePruebas().stream().map(CarpetaDePruebas::new).collect(Collectors.toList());
		return this.getPaginaCarpetaDePruebas(listadoTodasCarpetasDePruebas, indicePaginaSolicitada, numeroResultadosPagina);
	}
	
	private Pagina<List<PruebaComplementaria>> getPaginaPruebas(List<PruebaComplementaria> listadoPruebasTotales, int indicePaginaSolicitada, int numeroResultadosPagina) {
		if (listadoPruebasTotales.size() > 0 && listadoPruebasTotales.size() <= (numeroResultadosPagina * indicePaginaSolicitada)) throw new ArrayIndexOutOfBoundsException();
		List<PruebaComplementaria> listadoResultado = listadoPruebasTotales.subList(
				numeroResultadosPagina * indicePaginaSolicitada,
				numeroResultadosPagina * (indicePaginaSolicitada + 1) < listadoPruebasTotales.size()
					? numeroResultadosPagina * (indicePaginaSolicitada > 0 ? indicePaginaSolicitada + 1 : 1)
					: listadoPruebasTotales.size());
		Pagina<List<PruebaComplementaria>> pagina = new Pagina(listadoResultado, indicePaginaSolicitada, numeroResultadosPagina, listadoPruebasTotales.size());
		return pagina;
	}
	
	private Pagina<List<CarpetaDePruebas>> getPaginaCarpetaDePruebas(List<CarpetaDePruebas> listadoCarpetasDePruebasTotales, int indicePaginaSolicitada, int numeroResultadosPagina) {
		if (listadoCarpetasDePruebasTotales.size() > 0 && listadoCarpetasDePruebasTotales.size() <= (numeroResultadosPagina * indicePaginaSolicitada)) throw new ArrayIndexOutOfBoundsException();
		List<CarpetaDePruebas> listadoResultado = listadoCarpetasDePruebasTotales.subList(
				numeroResultadosPagina * indicePaginaSolicitada,
				numeroResultadosPagina * (indicePaginaSolicitada + 1) < listadoCarpetasDePruebasTotales.size()
					? numeroResultadosPagina * (indicePaginaSolicitada > 0 ? indicePaginaSolicitada + 1 : 1)
					: listadoCarpetasDePruebasTotales.size());
		Pagina<List<CarpetaDePruebas>> pagina = new Pagina(listadoResultado, indicePaginaSolicitada, numeroResultadosPagina, listadoCarpetasDePruebasTotales.size());
		return pagina;
	}
	
	private void annadirPruebaACarpetaDePruebas(Long idCarpeta, Long idPrueba) {
		Long idRelacionMasAlta = repositorioPruebas.getIdMasAltoRelacionPruebasCarpetaDePruebas();
		Long proximaIdRelacionDisponible = idRelacionMasAlta != null ? idRelacionMasAlta + 1 : 1;
		repositorioPruebas.annadirPruebaACarpetaDePruebas(proximaIdRelacionDisponible, idCarpeta, idPrueba);
	}
	
	private RequisitosPrueba getRequisitoSiYaExiste(RequisitosPrueba requisitoABuscar,List<RequisitosPrueba> listadoDeRequisitosExistentes) {
		for(RequisitosPrueba requisitoExistente : listadoDeRequisitosExistentes) {
			if(requisitoExistente.getPrueba() == requisitoABuscar.getPrueba()
			&& requisitoExistente.getEdadInicio() == requisitoABuscar.getEdadInicio()
			&& requisitoExistente.getEdadFin() == requisitoABuscar.getEdadFin()
			&& requisitoExistente.isMuyRecomendable() == requisitoABuscar.isMuyRecomendable())
				return requisitoExistente;
		}
		return null;
	}
	
}
