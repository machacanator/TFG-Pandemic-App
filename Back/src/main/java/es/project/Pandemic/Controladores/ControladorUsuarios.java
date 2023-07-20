package es.project.Pandemic.Controladores;

import java.util.List;
import java.util.Map;

import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.EntidadesYClasesSecundarias.Persona;
import es.project.Pandemic.Servicios.ServicioPersonas;
import es.project.Pandemic.Servicios.ServicioUsuarios;

@RestController
public class ControladorUsuarios {

	@Autowired
	ServicioUsuarios servicioUsuarios;

	@Autowired
	ServicioPersonas servicioPersonas;

	@Autowired
	ConfiguracionJWT JWT;

	@RequestMapping(value = "/Pandemic/usuario/nombre", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getNombreUsuario(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			
			return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
					.body(servicioPersonas.getNombre(tokenDecodificado.getSubject()));
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}

	@RequestMapping(value = "/Pandemic/usuario/notificaciones", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Object>> getNotificacionesUsuario(HttpServletRequest peticion,
			HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			return ResponseEntity.status(HttpStatus.OK)
					.body(servicioUsuarios.getNotificacionesUsuario(tokenDecodificado.getSubject()));
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}

	@RequestMapping(value = "/Pandemic/persona/{idPersona}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Persona> solicitarInfoPersona(@PathVariable(value = "idPersona") long idPersona,
			HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPersonas.solicitarInfoPersona(idPersona, tokenDecodificado.getSubject()));
			} catch (EntityNotFoundException e) {
				Errores.PERSONAS.PERSONA_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccessDeniedException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}

	@RequestMapping(value = "/Pandemic/administracion-plantilla", params = {"pagina", "resultados"}, method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Pagina<List<Persona>>> getPaginaPlantillaDeCentro(@RequestParam String pagina,
			@RequestParam String resultados, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioUsuarios.getPlantillaParaAdministrador(
						tokenDecodificado.getSubject(), Integer.parseInt(pagina), Integer.parseInt(resultados)));
			} catch (UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (IndexOutOfBoundsException e) {
				Errores.PAGINACION.PAGINA_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-plantilla", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<Map<String, String>>> getNombresPlantillaDeCentro(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioUsuarios.getNombresPlantillaCentroDeAdmin(tokenDecodificado.getSubject()));
			} catch (UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-plantilla/empleado", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Persona> getInfoCandidato(@RequestParam long id,
			HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPersonas.getPersona(id));
			} catch (EntityNotFoundException e) {
				Errores.PERSONAS.PERSONA_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccessDeniedException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-plantilla/candidato", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Persona> getInfoCandidato(@RequestParam String numeroSeguridadSocial,
			HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				
				return ResponseEntity.status(HttpStatus.OK).body(servicioPersonas.getInfoCandidato(numeroSeguridadSocial, tokenDecodificado.getSubject()));
			} catch (EntityNotFoundException e) {
				Errores.PERSONAS.PERSONA_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalAccessException e) {
				Errores.ADMINISTRACION.CANDIDATO_ES_EMPLEADO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccessDeniedException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-plantilla/eliminar", method = RequestMethod.DELETE)
	@ResponseBody
	ResponseEntity<String> eliminarEmpleadoDePlantilla(@RequestParam long id, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioUsuarios.eliminarEmpleadoComoAdministrador(id, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.EMPLEADOS.EMPLEADO_ELIMINADO); 
			} catch(AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.NO_EXISTE_EMPLEADO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (InvalidAttributeIdentifierException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-plantilla/annadir-candidato", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> annadirCandidatoAPlantilla(@RequestBody Map<String, String> datos, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioUsuarios.actualizarUsuarioPlantilla(datos, tokenDecodificado.getSubject(), false);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.EMPLEADOS.NUEVO_EMPLEADO_ANNADIDO); 
			} catch(AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.ERROR_IDENTIFICADOR_PRUEBA_TRATABLE.apply(e.getMessage()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (Exception e) {
				if(e.getMessage().equals(Errores.ADMINISTRACION.ERROR_NUMERO_SEGURIDAD_SOCIAL.getMensaje())) {
					 Errores.ADMINISTRACION.ERROR_NUMERO_SEGURIDAD_SOCIAL.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if (e.getMessage().equals(Errores.ADMINISTRACION.NO_EXISTE_CANDIDATO.getMensaje())) {
					Errores.ADMINISTRACION.NO_EXISTE_CANDIDATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if (e.getMessage().equals(Errores.ADMINISTRACION.CANDIDATO_ES_EMPLEADO.getMensaje())) {
					Errores.ADMINISTRACION.CANDIDATO_ES_EMPLEADO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if (e.getMessage().equals(Errores.ADMINISTRACION.ERROR_LISTA_PRUEBAS_TRATABLES.getMensaje())) {
					Errores.ADMINISTRACION.ERROR_LISTA_PRUEBAS_TRATABLES.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				}
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-plantilla/actualizar-empleado", method = RequestMethod.PATCH)
	@ResponseBody
	ResponseEntity<String> actualizarEmpleado(@RequestBody Map<String, String> datos, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioUsuarios.actualizarUsuarioPlantilla(datos, tokenDecodificado.getSubject(), true);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.EMPLEADOS.EMPLEADO_ACTUALIZADO); 
			} catch(AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.ERROR_IDENTIFICADOR_PRUEBA_TRATABLE.apply(e.getMessage()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (Exception e) {
				if(e.getMessage().equals(Errores.ADMINISTRACION.ERROR_NUMERO_SEGURIDAD_SOCIAL.getMensaje())) {
					 Errores.ADMINISTRACION.ERROR_NUMERO_SEGURIDAD_SOCIAL.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if (e.getMessage().equals(Errores.ADMINISTRACION.NO_EXISTE_EMPLEADO.getMensaje())) {
					Errores.ADMINISTRACION.NO_EXISTE_EMPLEADO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if (e.getMessage().equals(Errores.ADMINISTRACION.ERROR_LISTA_PRUEBAS_TRATABLES.getMensaje())) {
					Errores.ADMINISTRACION.ERROR_LISTA_PRUEBAS_TRATABLES.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				}
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/{idCentro}/nombres-administradores", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<String>> getNombresAdministradoresDeCentro(@PathVariable(value="idCentro") long idCentro, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPersonas.getNombresAdministradoresDeCentro(tokenDecodificado.getSubject(), idCentro));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/{idCentro}/administradores", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<Persona>> getAdministradoresDeCentro(@PathVariable(value="idCentro") long idCentro, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPersonas.getAdministradoresDeCentro(tokenDecodificado.getSubject(), idCentro));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/info-persona", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Persona> getInfoPersona(@RequestParam String numeroSeguridadSocial, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPersonas.getPersona(numeroSeguridadSocial));
			} catch (EntityNotFoundException e) {
				Errores.PERSONAS.PERSONA_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
}
