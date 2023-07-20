package es.project.Pandemic.Controladores;

import javax.naming.directory.AttributeModificationException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.Servicios.ServicioNotificaciones;

@RestController
public class ControladorNotificaciones {
	@Autowired
	ServicioNotificaciones servicioNotificaciones;
	
	@Autowired
	ConfiguracionJWT JWT;
	
	@RequestMapping(value = "/Pandemic/notificaciones/borrar/{idNotificacion}", method = RequestMethod.DELETE)
	public ResponseEntity<String> borrarNotificacion(HttpServletRequest peticion, HttpServletResponse respuesta, @PathVariable(value="idNotificacion") long idNotificacion) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				if(servicioNotificaciones.borrarNotificacionUsuario(idNotificacion, tokenDecodificado.getSubject()) ) {
					return ResponseEntity.status(HttpStatus.OK).body(Globales.NOTIFICACIONES.NOTIFICACION_BORRADA);
				} else {
					Errores.NOTIFICACIONES.ERROR_MARCAR_NOTIFICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
					return null;
				}
			} catch(EntityNotFoundException e) {
				Errores.NOTIFICACIONES.NOTIFICACION_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(IllegalAccessException e) {
				Errores.NOTIFICACIONES.ERROR_PERMISOS_NOTIFICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/notificaciones/marcar/{idNotificacion}", method = RequestMethod.POST)
	public ResponseEntity<String> marcarNotificacionVisualizada(HttpServletRequest peticion, HttpServletResponse respuesta, @PathVariable(value="idNotificacion") long idNotificacion) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				if(servicioNotificaciones.marcarNotificacionVisualizada(idNotificacion, tokenDecodificado.getSubject()) ) {
					return ResponseEntity.status(HttpStatus.OK).body(Globales.NOTIFICACIONES.NOTIFICACION_VISUALIZADA);
				} else {
					Errores.NOTIFICACIONES.ERROR_MARCAR_NOTIFICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
					return null;
				}
			} catch(EntityNotFoundException e) {
				Errores.NOTIFICACIONES.NOTIFICACION_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(IllegalAccessException e) {
				Errores.NOTIFICACIONES.ERROR_PERMISOS_NOTIFICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(AttributeModificationException e) {
				Errores.NOTIFICACIONES.NOTIFICACION_YA_VISUALIZADA.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
}
