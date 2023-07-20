package es.project.Pandemic.Controladores;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.EstadisticaCentro;
import es.project.Pandemic.EntidadesYClasesSecundarias.EstadisticaCentroComunidad;
import es.project.Pandemic.EntidadesYClasesSecundarias.EstadisticaPruebaEnCentro;
import es.project.Pandemic.Servicios.ServicioEstadisticas;

@RestController
public class ControladorEstadisticas {
		
	@Autowired
	ServicioEstadisticas servicioEstadisticas;
	
	@Autowired
	ConfiguracionJWT JWT;
	
	@RequestMapping(value = "/Pandemic/estadisticas", params = {"fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaCentro>> getEstadisticasEnfermero(@RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasEnfermero(tokenDecodificado.getSubject(), fecha, modoAnual)) ;
			} catch (AbortException e) {
				Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/estadisticas", params = {"usuario", "fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaCentro>> getEstadisticasEmpleado(@RequestParam Long usuario, @RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasEmpleado(tokenDecodificado.getSubject(), usuario, fecha, modoAnual)) ;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				if(e.getMessage().equals("fecha")) {
					Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if(e.getMessage().equals("idUsuario")) {
					Errores.ESTADISTICAS.USUARIO_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				}
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/estadisticas/centro/carpetas-pruebas", params = {"fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaPruebaEnCentro>> getEstadisticasCentroDeAdmin(@RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasCentroDeAdmin(tokenDecodificado.getSubject(), fecha, modoAnual, null)) ;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				if(e.getMessage().equals("fecha")) {
					Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if(e.getMessage().equals("idUsuario")) {
					Errores.ESTADISTICAS.USUARIO_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				}
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/estadisticas/centro/pruebas", params = {"id", "fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaPruebaEnCentro>> getEstadisticasCentroDeAdmin(@RequestParam Long id, @RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasCentroDeAdmin(tokenDecodificado.getSubject(), fecha, modoAnual, id)) ;
			} catch (AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				if(e.getMessage().equals("fecha")) {
					Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if(e.getMessage().equals("idUsuario")) {
					Errores.ESTADISTICAS.USUARIO_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				}
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/estadisticas/centro/carpetas-pruebas", params = {"centro", "fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaPruebaEnCentro>> getEstadisticasCentro(@RequestParam Long centro, @RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasCentro(centro, fecha, modoAnual, null)) ;
			} catch (IllegalArgumentException e) {
				if(e.getMessage().equals("fecha")) {
					Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if(e.getMessage().equals("centro")) {
					Errores.ESTADISTICAS.CENTRO_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				}
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/estadisticas/centro/pruebas", params = {"centro", "id",  "fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaPruebaEnCentro>> getEstadisticasCentro(@RequestParam Long centro, @RequestParam Long id, @RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasCentro(centro, fecha, modoAnual, id)) ;
			} catch (IllegalArgumentException e) {
				if(e.getMessage().equals("fecha")) {
					Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				} else if(e.getMessage().equals("centro")) {
					Errores.ESTADISTICAS.CENTRO_NO_EXISTE.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				}
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/estadisticas/comunidad", params = {"fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaCentroComunidad>> getEstadisticasCentrosComunidad(@RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasCentrosComunidad(fecha, modoAnual)) ;
			} catch (IllegalArgumentException e) {
				Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	/*Añadir estadisticas de centros para una carpeta y lueg*/
	@RequestMapping(value = "/Pandemic/estadisticas/comunidad/carpetas-pruebas", params = {"id", "fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaCentroComunidad>> getEstadisticasComunidadPorCarpeta(@RequestParam Long id, @RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasComunidadPorCarpeta(id, fecha, modoAnual)) ;
			} catch (IllegalArgumentException e) {
				Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/estadisticas/comunidad/pruebas", params = {"id", "fecha", "modoAnual"}, method = RequestMethod.GET)
	public ResponseEntity<List<EstadisticaCentroComunidad>> getEstadisticasComunidadPorPrueba(@RequestParam Long id, @RequestParam String fecha, @RequestParam boolean modoAnual, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioEstadisticas.getEstadisticasComunidadPorPrueba(id, fecha, modoAnual)) ;
			} catch (IllegalArgumentException e) {
				Errores.ESTADISTICAS.ERROR_FECHA_FORMATO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
}
