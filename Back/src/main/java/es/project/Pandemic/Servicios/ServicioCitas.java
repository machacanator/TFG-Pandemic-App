package es.project.Pandemic.Servicios;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Centro;
import es.project.Pandemic.EntidadesYClasesSecundarias.Cita;
import es.project.Pandemic.EntidadesYClasesSecundarias.CitaHistorico;
import es.project.Pandemic.EntidadesYClasesSecundarias.Horario;
import es.project.Pandemic.EntidadesYClasesSecundarias.Notificacion;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.EntidadesYClasesSecundarias.Persona;
import es.project.Pandemic.EntidadesYClasesSecundarias.PruebaComplementaria;
import es.project.Pandemic.EntidadesYClasesSecundarias.RelacionPruebaPaciente;
import es.project.Pandemic.EntidadesYClasesSecundarias.Rol;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Repositorios.RepositorioCitas;
import es.project.Pandemic.Repositorios.RepositorioNotificaciones;
import es.project.Pandemic.Repositorios.RepositorioPersonas;
import es.project.Pandemic.Repositorios.RepositorioPruebasComplementarias;
import es.project.Pandemic.Repositorios.RepositorioRelacionPruebasPacientes;
import es.project.Pandemic.Repositorios.RepositorioRoles;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;

@Service
public class ServicioCitas {
	
	@Autowired
	private ServicioCentros servicioCentros;

	@Autowired
    private RepositorioCitas repositorioCitas;
	
	@Autowired
    private RepositorioPersonas repositorioPersonas;
	
	@Autowired
    private RepositorioUsuarios repositorioUsuarios;
	
	@Autowired
	private RepositorioRoles repositorioRoles;
	
	@Autowired
	private RepositorioNotificaciones repositorioNotificaciones;
	
	@Autowired
    private RepositorioPruebasComplementarias repositorioPruebas;
	
	@Autowired
    private RepositorioRelacionPruebasPacientes repositorioRelacionesPruebasPacientes;
	
	//Devuelve la informacion completa de la cita sin importar quien lo pida
	public Cita getCitaPorId(long idCita) {
		return repositorioCitas.getCitaPorId(idCita);
	}
	
	//Devuelve la informacion completa de la cita comprobando los permisos del solicitante
	public Cita getCitaPorId(long idCita, String documentoIdentidad) throws AccessDeniedException {
		Persona persona = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidad);
		Boolean esSolicitanteEnfermero = repositorioRoles.getRolesUsuarioSinCentro(documentoIdentidad).stream().anyMatch(rol -> rol.getNombre().equals("Enfermero"));
		Cita infoCita = repositorioCitas.getCitaPorId(idCita);
		if(infoCita.getPaciente() != persona.getId() && !esSolicitanteEnfermero) throw new AccessDeniedException("");
		return infoCita;
	}
	
	//Devuelve la informacion completa de la cita comprobando los permisos del solicitante
	public Cita getCitaPorId(long idCita, long idPersona) throws AccessDeniedException {
		Cita infoCita = repositorioCitas.getCitaPorId(idCita);
		if(infoCita.getPaciente() == idPersona) throw new AccessDeniedException("");
		return infoCita;
	}
	
	public void pedirCitaUsuario(Map<String, String> formularioCita, String documentoIdentidad) throws ParseException, AccountNotFoundException, EntityNotFoundException, EntityExistsException, IllegalArgumentException, InvalidParameterException {		
		Cita citaSinGuardar = comprobarPeticionCitaValida(formularioCita, documentoIdentidad);
		citaSinGuardar.setId(repositorioCitas.getIdMasAlto() + 1);
		repositorioCitas.guardarNuevaCita(citaSinGuardar.getId(), citaSinGuardar.getPaciente(), citaSinGuardar.getPrueba(), citaSinGuardar.getFecha(), citaSinGuardar.getCentro(), citaSinGuardar.getEnfermero());
		Usuario usuario = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		Notificacion nuevaNotificacion = new Notificacion();
		nuevaNotificacion.setId(repositorioNotificaciones.getIdMasAlto() + 1);
		nuevaNotificacion.setFecha(new java.sql.Timestamp(new Date().getTime()));
		nuevaNotificacion.setVisualizada(false);
		nuevaNotificacion.setCodigo("nueva_cita_creada");
		nuevaNotificacion.setUsuario(usuario.getId());
		nuevaNotificacion.setIdReferencia(citaSinGuardar.getId());
		repositorioNotificaciones.guardarNuevaNotificacion(nuevaNotificacion.getId(), nuevaNotificacion.getVisualizada(), nuevaNotificacion.getCodigo(), nuevaNotificacion.getFecha(), nuevaNotificacion.getUsuario(), nuevaNotificacion.getIdReferencia());
	}
	
	public void cambiarCitaUsuario(Map<String, String> formularioCita, String documentoIdentidad) throws ParseException, AccountNotFoundException, EntityNotFoundException, EntityExistsException, IllegalArgumentException, InvalidParameterException {
		Cita citaSinGuardar = comprobarPeticionCitaValida(formularioCita, documentoIdentidad);
		citaSinGuardar.setId(repositorioCitas.getIdMasAlto() + 1);
		Cita citaAnterior = repositorioCitas.getCitaPorId(Long.parseLong(formularioCita.get("id")));
		citaAnterior.setEstado(Globales.ESTADOS_CITAS.CANCELADA_USUARIO);
		repositorioCitas.guardarNuevaCita(citaSinGuardar.getId(), citaSinGuardar.getPaciente(), citaSinGuardar.getPrueba(), citaSinGuardar.getFecha(), citaSinGuardar.getCentro(), citaSinGuardar.getEnfermero());
		repositorioCitas.actualizarCita(citaAnterior.getId(), citaAnterior.getPaciente(), citaAnterior.getPrueba(), citaAnterior.getFecha(), citaAnterior.getCentro(), citaAnterior.getEnfermero());
		Usuario usuario = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		Notificacion nuevaNotificacion = new Notificacion();
		System.out.println(repositorioNotificaciones.getIdMasAlto());
		nuevaNotificacion.setId(repositorioNotificaciones.getIdMasAlto() + 1);
		nuevaNotificacion.setFecha(new java.sql.Timestamp(new Date().getTime()));
		nuevaNotificacion.setVisualizada(false);
		nuevaNotificacion.setCodigo("cita_cambiada");
		nuevaNotificacion.setUsuario(usuario.getId());
		nuevaNotificacion.setIdReferencia(citaSinGuardar.getId());
		repositorioNotificaciones.guardarNuevaNotificacion(nuevaNotificacion.getId(), nuevaNotificacion.getVisualizada(), nuevaNotificacion.getCodigo(), nuevaNotificacion.getFecha(), nuevaNotificacion.getUsuario(), nuevaNotificacion.getIdReferencia());
	}
	
	public boolean cancelarCitaUsuario(long idCita, String documentoIdentidadPaciente) throws AccountNotFoundException {
		Persona paciente = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidadPaciente);
		if(paciente == null || paciente.getId() < 0) throw new AccountNotFoundException();
		Cita cita = this.getCitaPorId(idCita);
		if (cita.getPaciente() == paciente.getId()) {
			cita.setEstado(Globales.ESTADOS_CITAS.CANCELADA_USUARIO);
			repositorioCitas.saveAndFlush(cita);
			return true;
		}
		return false;
	}
	
	public void gestionarCita(Map<String, String> formularioCita, String documentoIdentidadEnfermero) throws AccountNotFoundException, InvalidParameterException {
		Persona enfermero = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidadEnfermero);
		if(enfermero == null || enfermero.getId() < 0) throw new AccountNotFoundException();
		Cita cita = this.getCitaPorId(Integer.parseInt(formularioCita.get("id")));
		if(cita == null) throw new InvalidParameterException("cita");
		Integer estado = Integer.parseInt(formularioCita.get("estado")); 
		if(estado != 1 && estado != -3) throw new InvalidParameterException("estado");
		cita.setEstado(estado);
		cita.setObservaciones(formularioCita.get("observaciones"));
		cita.setEnfermero(enfermero.getId());
		repositorioCitas.saveAndFlush(cita);
		
		//Buscar en la tabla de pruebas paciente y marcar la prueba como completada o incompleta
		RelacionPruebaPaciente relacionPruebaPaciente = repositorioRelacionesPruebasPacientes.getRelacionPruebaPaciente(cita.getPaciente(), cita.getPrueba());
		relacionPruebaPaciente.setCompletada(estado == 1);
		repositorioRelacionesPruebasPacientes.saveAndFlush(relacionPruebaPaciente);
	}
	
	public boolean estaPruebaCompletadaParaPaciente(Long idPrueba, Long idPaciente) {
		return repositorioRelacionesPruebasPacientes.estaPruebaCompletadaParaPaciente(idPrueba, idPaciente) > 0;
	}
	
	public Date getFechaUltimaCitaDePruebaParaPaciente(Long idPrueba, Long idPaciente) {
		List<Date> listaFechasCitas = repositorioCitas.getFechaUltimaCitaDePruebaParaPaciente(idPrueba, idPaciente);
		return listaFechasCitas.get(listaFechasCitas.size() - 1);
	}
	
	public Pagina<List<CitaHistorico>> getCitasPendientes(String documentoIdentidadPaciente, int indicePaginaSolicitada, int numeroResultadosPagina) throws AccountNotFoundException, ArrayIndexOutOfBoundsException {
		Persona paciente = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidadPaciente);
		if(paciente == null || paciente.getId() < 0) throw new AccountNotFoundException();
		List<CitaHistorico> listadoCitasTotales = repositorioCitas.getCitasPendientesPaciente(paciente.getId()).stream().map(CitaHistorico::new).collect(Collectors.toList());
		return this.getPaginaCitas(listadoCitasTotales, indicePaginaSolicitada, numeroResultadosPagina);
	}
	
	public Pagina<List<CitaHistorico>> getDiarioCitas(String documentoIdentidadPaciente, int indicePaginaSolicitada, int numeroResultadosPagina) throws AccountNotFoundException, ArrayIndexOutOfBoundsException {
		Usuario usuarioEnfermero = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidadPaciente);
		if(usuarioEnfermero == null || usuarioEnfermero.getId() < 0) throw new AccountNotFoundException();
		List<Long> listadoPruebasDisponiblesParaEnfermero = repositorioPruebas.getPruebasDisponiblesParaUsuario(usuarioEnfermero.getId());
		List<Long> listadoCentrosDisponiblesEnfermero = repositorioRoles.getCentrosParaRolUsuario(usuarioEnfermero.getId(), Globales.ROLES.ENFERMERO);
		List<CitaHistorico> listadoCitasTotales = new ArrayList<>();
		for(Long idCentro : listadoCentrosDisponiblesEnfermero) {
			for(Long idPrueba : listadoPruebasDisponiblesParaEnfermero) {
				listadoCitasTotales.addAll(repositorioCitas.getCitasDiario(idPrueba, idCentro, new Date()).stream().map(CitaHistorico::new).collect(Collectors.toList()));
			}
		}
		return this.getPaginaCitas(listadoCitasTotales, indicePaginaSolicitada, numeroResultadosPagina);
	}
	
	public Pagina<List<CitaHistorico>> getHistoricoCitas(String documentoIdentidad, String modo, int indicePaginaSolicitada, int numeroResultadosPagina) {
		List<CitaHistorico> listadoCitasTotales = new ArrayList<>();
		Persona persona = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidad);
		if(modo.equals(Globales.MODOS_HISTORICO.USUARIO)) {
			listadoCitasTotales = repositorioCitas.getHistoricoPaciente(persona.getId()).stream().map(CitaHistorico::new).collect(Collectors.toList());
		} else if (modo.equals(Globales.MODOS_HISTORICO.ENFERMERO)) {
			// Añadir codigo
			List<Rol> listaDeRolesDeUsuario = repositorioRoles.getRolesUsuarioSinCentro(documentoIdentidad);
			if(listaDeRolesDeUsuario.stream().filter(rol -> rol.getId() == Globales.ROLES.ENFERMERO).collect(Collectors.toList()).size() == 0) throw new AccessDeniedException("");
			listadoCitasTotales = repositorioCitas.getHistoricoCitasEnfermero(persona.getId()).stream().map(CitaHistorico::new).collect(Collectors.toList());
		} else if (modo.equals(Globales.MODOS_HISTORICO.ADMINISTRADOR)) {
			// Añadir codigo
			List<Rol> listaDeRolesDeUsuario = repositorioRoles.getRolesUsuarioSinCentro(documentoIdentidad);
			if(listaDeRolesDeUsuario.stream().filter(rol -> rol.getId() == Globales.ROLES.ADMINISTRADOR_HOSPITAL).collect(Collectors.toList()).size() == 0) throw new AccessDeniedException("");
			List<Long> centrosUsuario = repositorioRoles.getCentrosParaRolUsuario(persona.getUsuario(), Globales.ROLES.ADMINISTRADOR_HOSPITAL);
			listadoCitasTotales = new ArrayList<>();
			for(Long idCentro : centrosUsuario) {				
				listadoCitasTotales.addAll(repositorioCitas.getHistoricoCitasCentro(idCentro).stream().map(CitaHistorico::new).collect(Collectors.toList()));
			}
		}
		return this.getPaginaCitas(listadoCitasTotales, indicePaginaSolicitada, numeroResultadosPagina);
	}
	
	private Pagina<List<CitaHistorico>> getPaginaCitas(List<CitaHistorico> listadoCitasTotales, int indicePaginaSolicitada, int numeroResultadosPagina) {
		if (listadoCitasTotales.size() > 0 && listadoCitasTotales.size() <= (numeroResultadosPagina * indicePaginaSolicitada)) throw new ArrayIndexOutOfBoundsException();
		List<CitaHistorico> listadoResultado = listadoCitasTotales.subList(
				numeroResultadosPagina * indicePaginaSolicitada,
				numeroResultadosPagina * (indicePaginaSolicitada + 1) < listadoCitasTotales.size()
					? numeroResultadosPagina * (indicePaginaSolicitada > 0 ? indicePaginaSolicitada + 1 : 1)
					: listadoCitasTotales.size());
		Pagina<List<CitaHistorico>> pagina = new Pagina(listadoResultado, indicePaginaSolicitada, numeroResultadosPagina, listadoCitasTotales.size());
		return pagina;
	}
	
	private Cita comprobarPeticionCitaValida(Map<String, String> formularioCita, String documentoIdentidadPaciente) throws ParseException, AccountNotFoundException, EntityNotFoundException, EntityExistsException, IllegalArgumentException, InvalidParameterException {
		/* Comprobar que el paciente es valido*/
		Persona paciente = repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidadPaciente);
		if(paciente == null || paciente.getId() < 0) throw new AccountNotFoundException();
		
		/* Comprobar que la prueba complementaria es valida*/
		PruebaComplementaria prueba = repositorioPruebas.getPruebaComplementariaPorId(Integer.parseInt(formularioCita.get("prueba")));
		if(prueba == null || prueba.getId() < 0) throw new EntityNotFoundException("prueba"); 
		
		/* Comprobar que el centro es valido*/
		Centro centro = servicioCentros.getCentro(Integer.parseInt(formularioCita.get("centro")));
		if(centro == null || centro.getId() < 0) throw new EntityNotFoundException("centro");
		
		/* Comprobar que la fecha seleccionada es valida*/
		Timestamp fecha = new java.sql.Timestamp(Globales.FECHAS.FORMATO_FECHA_HORA.parse(formularioCita.get("fecha")).getTime());
		if(fecha.before(new Date())) throw new IllegalArgumentException();
		if(repositorioCitas.existeCita(prueba.getId(), centro.getId(), fecha) > 0) throw new EntityExistsException();
		
		/* Comprobar que la fecha seleccionada se encuentre de un hueco disponible en el caso de que la haya proporcionado*/
		List<Horario> listaHorariosDisponibles = centro.getHorarios().stream().filter(horarioPrueba -> horarioPrueba.getIdPrueba() == prueba.getId()).findFirst().get().getHorarios();
		Horario horarioAlQuePertenece = listaHorariosDisponibles.stream().filter(horario -> horario.estaDentroDelHorario(new java.sql.Time(fecha.getTime()))).findFirst().get();
		List<String> horasPosiblesCitas = horarioAlQuePertenece.getCitasDisponiblesHorario(prueba.getDuracionPrueba());
		boolean a = !horasPosiblesCitas.stream().anyMatch(hora -> {System.out.println(hora+" "+fecha.toString().contains(hora)); return fecha.toString().contains(hora);});
		if(!horasPosiblesCitas.stream().anyMatch(hora -> fecha.toString().contains(hora))) throw new InvalidParameterException();
		Cita cita =  new Cita();
		cita.setPaciente(paciente.getId());
		cita.setPrueba(prueba.getId());
		cita.setEstado(0);
		cita.setCentro(centro.getId());
		cita.setFecha(fecha);
		cita.setObservaciones(null);
		cita.setCodigoqr(null);
		cita.setEnfermero(null);
		return cita;
	}
	
	
	
}