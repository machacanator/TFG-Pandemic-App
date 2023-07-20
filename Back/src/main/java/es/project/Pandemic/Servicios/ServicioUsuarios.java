package es.project.Pandemic.Servicios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.security.auth.login.AccountNotFoundException;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Cita;
import es.project.Pandemic.EntidadesYClasesSecundarias.Notificacion;
import es.project.Pandemic.EntidadesYClasesSecundarias.NotificacionExtendida;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.EntidadesYClasesSecundarias.Persona;
import es.project.Pandemic.EntidadesYClasesSecundarias.PruebaComplementaria;
import es.project.Pandemic.EntidadesYClasesSecundarias.Rol;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Repositorios.RepositorioPersonas;
import es.project.Pandemic.Repositorios.RepositorioRoles;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;

@Service
public class ServicioUsuarios implements UserDetailsService{
	
	@Autowired
    private RepositorioUsuarios repositorioUsuarios;
	
	@Autowired
    private RepositorioRoles repositorioRoles;
	
	@Autowired
    private RepositorioPersonas repositorioPersonas;
	
	@Autowired
    private ServicioCitas servicioCitas;
	
	@Autowired
    private ServicioPruebasComplementarias servicioPruebasComplementarias;
	
	public boolean existeUsuario(String documentoIdentidad) throws UsernameNotFoundException {
		UserDetails userDetails = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		return userDetails != null;
	}
	
	@Override
	public UserDetails loadUserByUsername(String documentoIdentidad) throws UsernameNotFoundException {
		return getUsuarioPorDocumentoIdentidad(documentoIdentidad);
	}
	
	public List<Object> getNotificacionesUsuario(String documentoIdentidad) throws UsernameNotFoundException {
		List<Notificacion> notificacionesUsuario = this.getUsuarioPorDocumentoIdentidad(documentoIdentidad).getNotificaciones();
		List<Object> notificacionesExtendidas = new ArrayList<>();
		if(notificacionesUsuario != null && notificacionesUsuario.size() > 0) {
			notificacionesUsuario.forEach((notificacion) -> {
				if (notificacion.getCodigo().contains("cita") && notificacion.getIdReferencia() > 0) {
					Cita informacionCita = servicioCitas.getCitaPorId(notificacion.getIdReferencia());
					notificacionesExtendidas.add(informacionCita != null ? new NotificacionExtendida(notificacion, informacionCita) : notificacion);
				} else if (notificacion.getCodigo().contains("prueba") && notificacion.getIdReferencia() > 0) {
					PruebaComplementaria informacionPrueba = servicioPruebasComplementarias.getPruebaComplemetariaPorId(notificacion.getIdReferencia());
					notificacionesExtendidas.add(informacionPrueba != null ? new NotificacionExtendida(notificacion, informacionPrueba) : notificacion);
				} else {
					notificacionesExtendidas.add(notificacion);
				}
			});
		}
		return notificacionesExtendidas;
	}
	
	public Usuario getUsuarioPorDocumentoIdentidad(String documentoIdentidad) {
		Usuario usuario = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuario == null) throw new UsernameNotFoundException(Errores.LOGIN.USUARIO_NO_EXISTE.getMensaje());
		List<Rol> rolesUsuario = repositorioRoles.getRolesUsuarioSinCentro(usuario.getId());
		usuario.setRoles(rolesUsuario != null ? rolesUsuario : new ArrayList<Rol>());
        return usuario;
	}
	
	public List<Map<String, String>> getNombresPlantillaCentroDeAdmin(String documentoIdentidadPaciente) throws AccountNotFoundException {
		Usuario usuarioDelAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidadPaciente);
		if(usuarioDelAdministrador == null || usuarioDelAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long idCentroDelAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioDelAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		List<Long> listadoUsuariosDePlantilla = repositorioRoles.getPlantillaDeCentro(idCentroDelAdministrador);
		List<Map<String, String>> nombresPlantilla = new ArrayList();
		for(Long idUsuario : listadoUsuariosDePlantilla) {
			Persona personaEmpleado = repositorioPersonas.getPersonaConUsuario(idUsuario);
			Map<String, String> infoEmpleado = new HashMap();
			infoEmpleado.put("idUsuario", idUsuario.toString());
			infoEmpleado.put("nombre", personaEmpleado.getApellidos()+", "+personaEmpleado.getNombre());
			nombresPlantilla.add(infoEmpleado);
		}
		return nombresPlantilla;
	}
	
	public Pagina<List<Persona>> getPlantillaParaAdministrador(String documentoIdentidadPaciente, int indicePaginaSolicitada, int numeroResultadosPagina) throws AccountNotFoundException, ArrayIndexOutOfBoundsException {
		Usuario usuarioDelAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidadPaciente);
		if(usuarioDelAdministrador == null || usuarioDelAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long idCentroDelAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioDelAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		List<Long> listadoUsuariosDePlantilla = repositorioRoles.getPlantillaDeCentro(idCentroDelAdministrador);
		List<Persona> plantilla = new ArrayList();
		for(Long idUsuario : listadoUsuariosDePlantilla) {
			plantilla.add(repositorioPersonas.getPersonaConUsuario(idUsuario));
		}
		return this.getPaginaPlantilla(plantilla, indicePaginaSolicitada, numeroResultadosPagina);
	}
	
	private Pagina<List<Persona>> getPaginaPlantilla(List<Persona> listadoPersonasPlantilla, int indicePaginaSolicitada, int numeroResultadosPagina) throws ArrayIndexOutOfBoundsException {
		if (listadoPersonasPlantilla.size() > 0 && listadoPersonasPlantilla.size() <= (numeroResultadosPagina * indicePaginaSolicitada)) throw new ArrayIndexOutOfBoundsException();
		List<Persona> listadoResultado = listadoPersonasPlantilla.subList(
				numeroResultadosPagina * indicePaginaSolicitada,
				numeroResultadosPagina * (indicePaginaSolicitada + 1) < listadoPersonasPlantilla.size()
					? numeroResultadosPagina * (indicePaginaSolicitada > 0 ? indicePaginaSolicitada + 1 : 1)
					: listadoPersonasPlantilla.size());
		Pagina<List<Persona>> pagina = new Pagina(listadoResultado, indicePaginaSolicitada, numeroResultadosPagina, listadoPersonasPlantilla.size());
		return pagina;
	}
	
	public void eliminarEmpleadoComoAdministrador(long idEmpleadoAElimininar, String documentoIdentidadPaciente) throws AccountNotFoundException, AbortException, IllegalArgumentException, InvalidAttributeIdentifierException{
		Usuario usuarioAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidadPaciente);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long centroDeAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(centroDeAdministrador == null || centroDeAdministrador < 0) throw new AbortException();
		if(!repositorioUsuarios.existsById(idEmpleadoAElimininar)) throw new IllegalArgumentException();
		Long idPermiso = repositorioRoles.getIdPermisoEmpleado(idEmpleadoAElimininar, centroDeAdministrador);
		if(idPermiso == null || idPermiso < 0) throw new InvalidAttributeIdentifierException();
		repositorioRoles.eliminarPermiso(idPermiso);
		actualizarPruebasTratablesPorUsuarioTrasSerEliminado(idEmpleadoAElimininar);
	}
	
	public void actualizarUsuarioPlantilla(Map<String, String> formulario, String documentoIdentidad, boolean esEmpleadoUsuarioAActualizar) throws AccountNotFoundException, AbortException, Exception, IllegalArgumentException {
		Usuario usuarioAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
		if(usuarioAdministrador == null || usuarioAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long centroDeAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(centroDeAdministrador == null || centroDeAdministrador < 0) throw new AbortException();
		List<Long> listaIdsPruebasATratar = Arrays.asList(formulario.get("pruebasTratables").split(":")).stream().map((String idPrueba) -> Long.valueOf(idPrueba)).collect(Collectors.toList());
		comprobarDatosFormularioEmpleadoValidos(formulario.get("seguridadSocial"), listaIdsPruebasATratar, esEmpleadoUsuarioAActualizar, centroDeAdministrador);
		Long idUsuario = repositorioPersonas.getPersonaPorNumeroSeguridadSocial(formulario.get("seguridadSocial")).getUsuario();
		if(esEmpleadoUsuarioAActualizar) {
			actualizarPruebasTratablesEmpleado(idUsuario, listaIdsPruebasATratar);
		} else {
			repositorioRoles.asignarPermiso(repositorioRoles.getIdMasAltoPermisos() + 1, idUsuario, Globales.ROLES.ENFERMERO, centroDeAdministrador);
			annadirPruebasANuevoEmpleado(idUsuario, listaIdsPruebasATratar);
		}
	}
	
	private void annadirPruebasANuevoEmpleado(Long idUsuario, List<Long> listaIdsPruebasAAsignar) {
		Long siguienteId = servicioPruebasComplementarias.getIdMasAltoEnfermeroPruebas() + 1;
		for(Long idPrueba : listaIdsPruebasAAsignar) {
			servicioPruebasComplementarias.asignarNuevaPruebaAEnfermero(siguienteId, idPrueba, idUsuario);
			siguienteId += 1;
		}
	}
	
	private void actualizarPruebasTratablesEmpleado(Long idUsuarioEmpleado, List<Long> nuevaListaDePruebasDelEmpleado) {
		List<Long> listaActualDePruebasDelEmpleado = servicioPruebasComplementarias.getIdsPruebasTratablesPorEmpleado(idUsuarioEmpleado);
		List<Long> listaPruebasNuevasAAsignar = nuevaListaDePruebasDelEmpleado.stream().filter((Long idNuevaPrueba) -> !listaActualDePruebasDelEmpleado.contains(idNuevaPrueba)).collect(Collectors.toList());
		List<Long> listaPruebasADesasignar = listaActualDePruebasDelEmpleado.stream().filter((Long idPrueba) -> !nuevaListaDePruebasDelEmpleado.contains(idPrueba)).collect(Collectors.toList());
		for(Long idPrueba : listaPruebasADesasignar) {
			servicioPruebasComplementarias.desasignarPruebaAEnfermero(idPrueba, idUsuarioEmpleado);
		}
		Long siguienteId = servicioPruebasComplementarias.getIdMasAltoEnfermeroPruebas() + 1;
		for(Long idPrueba : listaPruebasNuevasAAsignar) {
			servicioPruebasComplementarias.asignarNuevaPruebaAEnfermero(siguienteId, idPrueba, idUsuarioEmpleado);
			siguienteId += 1;				
		}
	}
	
	private void actualizarPruebasTratablesPorUsuarioTrasSerEliminado(Long idUsuario) {
		List<Long> listadoCentroTrabajaUsuario = repositorioRoles.getCentrosParaRolUsuario(idUsuario, Globales.ROLES.ENFERMERO); 
		List<Long> listadoPruebasQueElEmpleadoDebePoderTratar = new ArrayList();
		for(Long idCentro : listadoCentroTrabajaUsuario) {
			List<Long> pruebasTratablesEnCentro = servicioPruebasComplementarias.getPruebasTratablesEnCentro(idCentro);
			for(Long idPrueba : pruebasTratablesEnCentro) {
				if(listadoPruebasQueElEmpleadoDebePoderTratar.contains(idPrueba)) listadoPruebasQueElEmpleadoDebePoderTratar.add(idPrueba);
			}
		}
		List<Long> listadoActualDePruebasTratablesPorEmpleado = servicioPruebasComplementarias.getIdsPruebasTratablesPorEmpleado(idUsuario);
		List<Long> listadoPruebasADesasignar = listadoActualDePruebasTratablesPorEmpleado.stream().filter((Long idPrueba) -> !listadoPruebasQueElEmpleadoDebePoderTratar.contains(idPrueba)).collect(Collectors.toList());
		for(Long idPrueba : listadoPruebasADesasignar) {
			servicioPruebasComplementarias.desasignarPruebaAEnfermero(idPrueba, idUsuario);
		}
	}
	
	private boolean comprobarDatosFormularioEmpleadoValidos(String numeroSeguridadSocial, List<Long> listaPruebasATratar, boolean esEmpleado, Long centro) throws Exception, IllegalArgumentException {
		if(numeroSeguridadSocial.length() != 9) throw new Exception(Errores.ADMINISTRACION.ERROR_NUMERO_SEGURIDAD_SOCIAL.getMensaje());
		if(repositorioPersonas.existePersona(numeroSeguridadSocial) == 0) throw new Exception(esEmpleado ? Errores.ADMINISTRACION.NO_EXISTE_EMPLEADO.getMensaje() : Errores.ADMINISTRACION.NO_EXISTE_CANDIDATO.getMensaje());
		if(!esEmpleado && repositorioRoles.esUsuarioEmpleadoDeCentro(repositorioPersonas.getPersonaPorNumeroSeguridadSocial(numeroSeguridadSocial).getUsuario(), centro) > 0) throw new Exception(Errores.ADMINISTRACION.CANDIDATO_ES_EMPLEADO.getMensaje());
		if(listaPruebasATratar.size() == 0) throw new Exception(Errores.ADMINISTRACION.ERROR_LISTA_PRUEBAS_TRATABLES.getMensaje());
		for(Long idPrueba : listaPruebasATratar) {
			if(!servicioPruebasComplementarias.existePrueba(idPrueba)) throw new IllegalArgumentException(idPrueba.toString());
		}
		return true;
	}
}
