package es.project.Pandemic.Controladores;

import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.bridge.AbortException;
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
import com.fasterxml.jackson.databind.JsonNode;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.CarpetaDePruebas;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.EntidadesYClasesSecundarias.PruebaComplementaria;
import es.project.Pandemic.EntidadesYClasesSecundarias.RequisitosPrueba;
import es.project.Pandemic.Servicios.ServicioPruebasComplementarias;

@RestController
public class ControladorPruebasComplementarias {
	@Autowired
	ConfiguracionJWT JWT;
	
	@Autowired
	ServicioPruebasComplementarias servicioPruebas;
	
	@RequestMapping(value = "/Pandemic/pruebas", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<PruebaComplementaria>> getTodasPruebasComplementarias(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getTodasPruebasComplementarias());
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/carpetas-pruebas", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<CarpetaDePruebas>> getTodasCarpetasDePruebas(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getTodasCarpetasPruebas());
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/pruebas/informacion", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<PruebaComplementaria>> getInformacionPruebasDisponibles(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getPruebasTratablesEnMiCentro(tokenDecodificado.getSubject()));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/pruebas/{idPrueba}", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<PruebaComplementaria> getInfoPrueba(@PathVariable(value="idPrueba") long idPrueba,HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getPruebaComplemetariaPorId(idPrueba));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/pruebas/paciente", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<JsonNode>> getPruebasDisponibles(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getPruebasIncompletasSinCitaPaciente(tokenDecodificado.getSubject()));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/pruebas/pendientes/{idPaciente}", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<String>> getPruebasPendientes(@PathVariable(value="idPaciente") long idPaciente, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getPruebasPendientes(idPaciente));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/pruebas/realizadas/{idPaciente}", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<String>> getPruebasRealizadas(@PathVariable(value="idPaciente") long idPaciente, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getPruebasRealizadas(idPaciente));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-plantilla/pruebas-empleado", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<String>> getPruebasTratablesPorEmpleado(@RequestParam Long id, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getPruebasTratablesPorEmpleado(id));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-pruebas/pruebas-centro", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<Long>> getPruebasQuePuedeTratarCentroDeAdmin(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getPruebasQuePuedeTratarCentroDeAdmin(tokenDecodificado.getSubject()));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(AccessDeniedException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/carpetas-de-pruebas-comunidad", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Pagina<List<CarpetaDePruebas>>> getPaginaCarpetasDePruebas(@RequestParam String pagina, @RequestParam String resultados, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getListadoCarpetasDePruebas(tokenDecodificado.getSubject(), Integer.parseInt(pagina), Integer.parseInt(resultados)));
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/pruebas-comunidad", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Pagina<List<PruebaComplementaria>>> getPaginaPruebasDecarpetaDePruebas(@RequestParam Long idCarpeta, @RequestParam String pagina, @RequestParam String resultados, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {				
				return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getListadoPruebasDeCarpetaDePruebas(tokenDecodificado.getSubject(), Integer.parseInt(pagina), Integer.parseInt(resultados), idCarpeta));
			} catch(IllegalArgumentException e) {
				Errores.ADMINISTRACION.NO_EXISTE_CARPETA_DE_PRUEBAS.enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/carpetas-de-pruebas-comunidad/annadir-carpeta-de-pruebas", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> nuevaCarpetaDePruebas(@RequestBody Map<String, String> formulario, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {			
				servicioPruebas.crearNuevaCarpetaDePruebas(formulario);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.CARPETA_PRUEBAS_ANNADIDA);
			} catch(IllegalArgumentException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/pruebas-comunidad/annadir-prueba", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> nuevaPrueba(@RequestParam Long idCarpeta, @RequestBody JsonNode formulario, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioPruebas.crearNuevaPrueba(idCarpeta, formulario);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.PRUEBA_ANNADIDA);
			} catch(IllegalArgumentException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			} catch (DataFormatException e) {
				String parametro = e.getMessage().split(";")[0];
				String indice = e.getMessage().split(";")[1];
				Errores.ADMINISTRACION.ERROR_PARAMETRO_REQUISITO.apply(parametro, indice).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				e.printStackTrace();
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/carpetas-de-pruebas-comunidad/actualizar-carpeta-de-pruebas", method = RequestMethod.PATCH)
	@ResponseBody
	ResponseEntity<String> actualizarCarpetaDePruebas(@RequestBody Map<String, String> formulario, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {			
				servicioPruebas.actualizarCarpetaDePruebas(formulario);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.CARPETA_PRUEBAS_ACTUALIZADA);
			} catch(IllegalArgumentException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			} catch(EntityNotFoundException e) {
				Errores.ADMINISTRACION.NO_EXISTE_CARPETA_DE_PRUEBAS.enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/pruebas-comunidad/actualizar-prueba", method = RequestMethod.PATCH)
	@ResponseBody
	ResponseEntity<String> actualizarPrueba(@RequestBody JsonNode formulario, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {			
				servicioPruebas.actualizarPrueba(formulario);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.PRUEBA_ACTUALIZADA);
			} catch(IllegalArgumentException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			} catch(EntityNotFoundException e) {
				Errores.ADMINISTRACION.NO_EXISTE_PRUEBA.enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			} catch (DataFormatException e) {
				String parametro = e.getMessage().split(";")[0];
				String indice = e.getMessage().split(";")[1];
				Errores.ADMINISTRACION.ERROR_PARAMETRO_REQUISITO.apply(parametro, indice).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				e.printStackTrace();
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/carpetas-de-pruebas-comunidad/eliminar", method = RequestMethod.DELETE)
	@ResponseBody
	ResponseEntity<String> eliminarCarpetaDePruebas(@RequestParam(value = "id") Long idCarpeta, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {			
				servicioPruebas.eliminarCarpetaDePruebas(idCarpeta);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.CARPETA_PRUEBAS_ELIMINADA);
			} catch(IllegalArgumentException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			} catch(EntityNotFoundException e) {
				Errores.ADMINISTRACION.NO_EXISTE_CARPETA_DE_PRUEBAS.enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/pruebas-comunidad/eliminar", method = RequestMethod.DELETE)
	@ResponseBody
	ResponseEntity<String> eliminarPrueba(@RequestParam(value = "id") Long idCarpeta, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {			
				servicioPruebas.eliminarPrueba(idCarpeta);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.PRUEBA_ELIMINADA);
			} catch(IllegalArgumentException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			} catch(EntityNotFoundException e) {
				Errores.ADMINISTRACION.NO_EXISTE_CARPETA_DE_PRUEBAS.enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/pruebas-comunidad/requisitos", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<RequisitosPrueba>> getRequisitosDePruebas(@RequestParam Long id, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				List<RequisitosPrueba> listaRequisitos = servicioPruebas.getRequisitosDePrueba(id);
				return ResponseEntity.status(HttpStatus.OK).body(listaRequisitos);
				//return ResponseEntity.status(HttpStatus.OK).body(servicioPruebas.getRequisitosDePrueba(id));
			} catch(IllegalArgumentException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply(e.getMessage()).enviar(HttpServletResponse.SC_BAD_REQUEST, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
}
