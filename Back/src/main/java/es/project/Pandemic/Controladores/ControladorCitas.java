package es.project.Pandemic.Controladores;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import es.project.Pandemic.EntidadesYClasesSecundarias.Cita;
import es.project.Pandemic.EntidadesYClasesSecundarias.CitaHistorico;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.Servicios.ServicioCitas;

@RestController
public class ControladorCitas {
	
	@Autowired
	ConfiguracionJWT JWT;
	
	@Autowired
	ServicioCitas servicioCitas;
	

	@RequestMapping(value = "/Pandemic/citas/{idCita}", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Cita> getCita(@PathVariable(value="idCita") long idCita, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCitas.getCitaPorId(idCita, tokenDecodificado.getSubject()));
			} catch (AccessDeniedException  e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/citas/pedirCita", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> pedirCitaUsuario(@RequestBody Map<String, String> formularioCita, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioCitas.pedirCitaUsuario(formularioCita, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.CITAS.CITA_SOLICITADA);
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (ParseException | IllegalArgumentException e) {
				Errores.CITAS.ERROR_FECHA_INVALIDA.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (EntityNotFoundException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (EntityExistsException e) {
				Errores.CITAS.ERROR_HORA_INVALIDA.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/citas/cambiarCita", method = RequestMethod.PATCH)
	@ResponseBody
	/*Comprobar que sea el paciente de la cita o que tenga permisos para modificarla*/
	ResponseEntity<String> cambiarCitaUsuario(@RequestBody Map<String, String> formularioCita, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioCitas.cambiarCitaUsuario(formularioCita, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.CITAS.CITA_CAMBIADA);
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (ParseException e) {
				Errores.CITAS.ERROR_FECHA_INVALIDA.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (EntityNotFoundException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (EntityExistsException e) {
				Errores.CITAS.ERROR_HORA_INVALIDA.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/citas/asignarCita", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> asignarCitaUsuario(@RequestBody Map<String, String> formularioCita, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioCitas.pedirCitaUsuario(formularioCita, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.CITAS.CITA_SOLICITADA);	
			} catch(AccountNotFoundException | UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (EntityNotFoundException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (EntityExistsException | InvalidParameterException e) {
				Errores.CITAS.ERROR_HORA_INVALIDA.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (IllegalArgumentException | ParseException e) {
				Errores.CITAS.ERROR_FECHA_INVALIDA.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/citas/eliminar", method = RequestMethod.DELETE)
	@ResponseBody
	ResponseEntity<String> eliminarCita(@RequestParam long id, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				if(servicioCitas.cancelarCitaUsuario(id, tokenDecodificado.getSubject()))
				return ResponseEntity.status(HttpStatus.OK).body(Globales.CITAS.CITA_ELIMINADA);
			} catch(UsernameNotFoundException e) {
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
	
	@RequestMapping(value = "/Pandemic/citas/pendientes", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Pagina<List<CitaHistorico>>> getCitasPendientes(@RequestParam String pagina, @RequestParam String resultados, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCitas.getCitasPendientes(tokenDecodificado.getSubject(), Integer.parseInt(pagina), Integer.parseInt(resultados)));
			} catch(UsernameNotFoundException e) {
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
	
	@RequestMapping(value = "/Pandemic/citas/diario", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Pagina<List<CitaHistorico>>> getDiarioCitas(@RequestParam String pagina, @RequestParam String resultados, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCitas.getDiarioCitas(tokenDecodificado.getSubject(), Integer.parseInt(pagina), Integer.parseInt(resultados)));
			} catch(UsernameNotFoundException e) {
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
	
	@RequestMapping(value = "/Pandemic/citas/historico", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Pagina<List<CitaHistorico>>> getHistoricoCitas(@RequestParam String modo, @RequestParam String pagina, @RequestParam String resultados, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCitas.getHistoricoCitas(tokenDecodificado.getSubject(), modo, Integer.parseInt(pagina), Integer.parseInt(resultados)));
			} catch(UsernameNotFoundException e) {
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
	
	@RequestMapping(value = "/Pandemic/citas/gestionarCita", method = RequestMethod.PATCH)
	@ResponseBody
	ResponseEntity<String> gestionarCita(@RequestBody Map<String, String> formularioCita, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioCitas.gestionarCita(formularioCita, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.CITAS.CITA_GESTIONADA);
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (InvalidParameterException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
}
