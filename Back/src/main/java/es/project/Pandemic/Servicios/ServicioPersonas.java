package es.project.Pandemic.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.AccountNotFoundException;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Persona;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Repositorios.RepositorioPersonas;
import es.project.Pandemic.Repositorios.RepositorioRoles;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;

@Service
public class ServicioPersonas {

	@Autowired
    private RepositorioPersonas repositorioPersonas;
	
	@Autowired
    private RepositorioRoles repositorioRoles;
	
	@Autowired
    private RepositorioUsuarios repositorioUsuarios;
	
	public List<Persona> getTodasPersonas() {
		return this.repositorioPersonas.findAll();
	}
	
	public String getNombre(String documentoIdentidad) throws UsernameNotFoundException {
		return repositorioPersonas.getNombreUsuario(documentoIdentidad);
	}
	
	public Persona getPersona(long idPersona) {
		return repositorioPersonas.getPersona(idPersona);
	}
	
	public Persona solicitarInfoPersona(long idPersona, String documentoIdentidadSolicitante) throws EntityNotFoundException, AccessDeniedException {
		Boolean esSolicitanteEnfermero = repositorioRoles.getRolesUsuarioSinCentro(documentoIdentidadSolicitante).stream().anyMatch(rol -> rol.getNombre().equals("Enfermero"));
		Persona infoPersona = this.getPersona(idPersona);
		if (infoPersona == null)
			throw new EntityNotFoundException();
		if (!infoPersona.getDocumentoIdentidad().equals(documentoIdentidadSolicitante) && !esSolicitanteEnfermero)
			throw new AccessDeniedException("");
		return infoPersona;
	}
	
	public Persona getPersona(String numeroSeguridadSocial) {
		return repositorioPersonas.getPersonaPorNumeroSeguridadSocial(numeroSeguridadSocial);
	}
	
	public Persona getPersonaPorDocumentoIdentidad(String documentoIdentidad) throws UsernameNotFoundException {
		return repositorioPersonas.getPersonaPorDocumentoIdentidad(documentoIdentidad);
	}
	
	public Persona getInfoCandidato(String numeroSeguridadSocial, String documentoIdentidadAdmin) throws EntityNotFoundException, AccountNotFoundException, AbortException, IllegalAccessException {
		Persona infoPersona = repositorioPersonas.getPersonaPorNumeroSeguridadSocial(numeroSeguridadSocial);
		if(infoPersona == null) throw new EntityNotFoundException();
		Usuario usuarioDelAdministrador = repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidadAdmin);
		if(usuarioDelAdministrador == null || usuarioDelAdministrador.getId() < 0) throw new AccountNotFoundException();
		Long idCentroDelAdministrador = repositorioRoles.getCentrosParaRolUsuario(usuarioDelAdministrador.getId(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(idCentroDelAdministrador == null || idCentroDelAdministrador < 0) throw new AbortException();
		if(repositorioRoles.esUsuarioEmpleadoDeCentro(infoPersona.getUsuario(), idCentroDelAdministrador) > 0) throw new IllegalAccessException();
		return infoPersona;
	}
	
	public Persona getInfoAdministrador(String numeroSeguridadSocial, String documentoIdentidadAdministradorComunidad) throws EntityNotFoundException, AccountNotFoundException, AbortException, IllegalAccessException {
		Persona infoPersonaAdministradorHospital = repositorioPersonas.getPersonaPorNumeroSeguridadSocial(numeroSeguridadSocial);
		if(infoPersonaAdministradorHospital == null) throw new EntityNotFoundException();
		if(infoPersonaAdministradorHospital.getUsuario() < 0) throw new AccountNotFoundException();
		Long idCentroDelAdministrador = repositorioRoles.getCentrosParaRolUsuario(infoPersonaAdministradorHospital.getUsuario(), Globales.ROLES.ADMINISTRADOR_HOSPITAL).get(0);
		if(idCentroDelAdministrador != null && idCentroDelAdministrador > 0) throw new AbortException();
		if(repositorioRoles.esUsuarioEmpleadoDeCentro(infoPersonaAdministradorHospital.getUsuario(), idCentroDelAdministrador) > 0) throw new IllegalAccessException();
		return infoPersonaAdministradorHospital;
	}
	
	public List<Persona> getAdministradoresDeCentro(String documentoIdentidad, Long idCentro) {
		List<Long> listaIdsUsuariosAdministradores = repositorioRoles.getAdministradoresDeCentro(idCentro);
		return listaIdsUsuariosAdministradores != null
				? listaIdsUsuariosAdministradores.stream().map(idUsuarioAdministrador -> repositorioPersonas.getPersonaConUsuario(idUsuarioAdministrador)).collect(Collectors.toList())
				: new ArrayList<>();
	}
	
	public List<String> getNombresAdministradoresDeCentro(String documentoIdentidad, Long idCentro) {
		return getAdministradoresDeCentro(documentoIdentidad, idCentro).stream().map(administrador -> administrador.getNombre()+" "+administrador.getApellidos()).collect(Collectors.toList());
	}
}
