package es.project.Pandemic.Servicios;

import javax.naming.directory.AttributeModificationException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.project.Pandemic.EntidadesYClasesSecundarias.Notificacion;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Repositorios.RepositorioNotificaciones;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;
@Service
public class ServicioNotificaciones {
		
	@Autowired
    private RepositorioNotificaciones repositorioNotificaciones;
	
	@Autowired
    private RepositorioUsuarios repositorioUsuarios;
	
	public boolean borrarNotificacionUsuario(long idNotificacion, String documentoIdentidad) throws EntityNotFoundException, IllegalAccessException, UsernameNotFoundException {
			Notificacion notificacion = this.repositorioNotificaciones.getNotificacion(idNotificacion);
			Usuario usuario = this.repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
			if(notificacion == null) throw new EntityNotFoundException();
			if(usuario == null) throw new UsernameNotFoundException(documentoIdentidad);
			if(!usuario.getNotificaciones().contains(notificacion)) throw new IllegalAccessException();				
			repositorioNotificaciones.borrarNotificacion(idNotificacion);
			return true;
	}
	
	public boolean marcarNotificacionVisualizada(long idNotificacion, String documentoIdentidad) throws EntityNotFoundException, IllegalAccessException, AttributeModificationException, UsernameNotFoundException {
		
			Notificacion notificacion = this.repositorioNotificaciones.getNotificacion(idNotificacion);
			Usuario usuario = this.repositorioUsuarios.findByDocumentoIdentidad(documentoIdentidad);
			if(notificacion == null) throw new EntityNotFoundException();
			if(usuario == null) throw new UsernameNotFoundException(documentoIdentidad);
			if(!usuario.getNotificaciones().contains(notificacion)) throw new IllegalAccessException();
			if(notificacion.getVisualizada()) throw new AttributeModificationException();
			repositorioNotificaciones.marcarNotificacionVisualizada(idNotificacion);
			return true;
	}
}