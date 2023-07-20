package es.project.Pandemic.Controladores;

import java.io.IOException;
import java.text.ParseException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.security.auth.login.AccountNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.auth0.jwt.interfaces.DecodedJWT;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Centro;
import es.project.Pandemic.EntidadesYClasesSecundarias.HorarioPruebaCentro;
import es.project.Pandemic.EntidadesYClasesSecundarias.HorariosPrueba;
import es.project.Pandemic.EntidadesYClasesSecundarias.ImagenCentro;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.Servicios.ServicioCentros;

@RestController
public class ControladorCentros {
	@Autowired
	ConfiguracionJWT JWT;
	
	@Autowired
	ServicioCentros servicioCentros;
	
	@RequestMapping(value = "/Pandemic/centros", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<Centro>> getTodosLosCentros(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getTodosLosCentros());
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/centros/{idCentro}", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Centro> getCentro(@PathVariable(value="idCentro") long idCentro, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getCentro(idCentro));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/centros/mi-centro", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Centro> getMiCentro(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getMiCentro(tokenDecodificado.getSubject()));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/centros/mi-centro/imagenes", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<ImagenCentro>> getImagenesDeMiCentro(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getImagenesDeMiCentro(tokenDecodificado.getSubject())); 			
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/centros/prueba/{idPrueba}", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<Centro>> getTodosLosCentrosConPrueba(@PathVariable(value="idPrueba") long idPrueba, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getCentrosConPrueba(idPrueba));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/centros/{idCentro}/horas", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<List<String>> getHorasCentroParaPrueba(@PathVariable(value="idCentro") long idCentro, @RequestBody Map<String, String> datos, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getHorasDisponibles(idCentro, Integer.parseInt(datos.get("prueba")), datos.get("fecha")));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (NumberFormatException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply("prueba").enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);                
			} catch (ParseException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply("fecha").enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
			}
		} else {			
			Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		}
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/centros/{idCentro}/dias", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<List<String>> getDiasDisponiblesCentroParaPrueba(@PathVariable(value="idCentro") long idCentro, @RequestBody Map<String, String> datos, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				YearMonth fechaAnnoMes = YearMonth.of(Integer.parseInt(datos.get("anno")), Integer.parseInt(datos.get("mes")));
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getDiasDisponibles(idCentro, Integer.parseInt(datos.get("prueba")), fechaAnnoMes));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (ParseException e) {
				Errores.PARAMETROS.PARAMETRO_NO_VALIDO.apply("fecha").enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
			} catch (NumberFormatException e) {
				Errores.PARAMETROS.PARAMETROS_NO_VALIDOS.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);                
			} 
		} else {			
			Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		}
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-centro", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<String> getNombreCentroDeAdmin(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getNombreCentroDeAdmin(tokenDecodificado.getSubject()));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(AccessDeniedException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-pruebas/pruebas-centro/horarios", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Pagina<List<HorariosPrueba>>> getPruebasYHorariosCentroDeAdmin(@RequestParam String pagina, @RequestParam String resultados, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getPruebasYHorariosCentroDeAdmin(tokenDecodificado.getSubject(), Integer.parseInt(pagina), Integer.parseInt(resultados)));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(AccessDeniedException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-pruebas/pruebas-centro/{idPrueba}", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<HorarioPruebaCentro>> getPruebasYHorariosCentroDeAdmin(@PathVariable(value="idPrueba") long idPrueba, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getHorariosPruebaEnCentroDeAdmin(tokenDecodificado.getSubject(), idPrueba));
			} catch(UsernameNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch(AccessDeniedException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.ERROR_IDENTIFICADOR_PRUEBA_TRATABLE.apply(String.valueOf(idPrueba)).enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-pruebas/pruebas-centro/eliminar", method = RequestMethod.DELETE)
	@ResponseBody
	ResponseEntity<String> eliminarPruebaCentro(@RequestParam long id, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioCentros.eliminarPruebaCentroComoAdministrador(id, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.PRUEBA_CENTRO_ELIMINADA); 
			} catch(AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.NO_EXISTE_PRUEBA_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (InvalidAttributeIdentifierException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-pruebas/pruebas-centro/annadir-prueba", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> annadirPruebaACentro(@RequestBody Map<String, String> formulario, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioCentros.annadirPruebaACentro(formulario, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.PRUEBA_CENTRO_ANNADIDA); 
			} catch(AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.NO_EXISTE_PRUEBA_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (DataFormatException e) {
				Errores.ADMINISTRACION.ERROR_HORARIOS_PRUEBA_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-pruebas/pruebas-centro/actualizar-horarios-prueba", method = RequestMethod.PATCH)
	@ResponseBody
	ResponseEntity<String> actualizarPruebaDeCentro(@RequestBody Map<String, String> formulario, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			try {
				servicioCentros.actualizarPruebaDeCentro(formulario, tokenDecodificado.getSubject());
				return ResponseEntity.status(HttpStatus.OK).body(Globales.PRUEBAS.PRUEBA_CENTRO_ACTUALIZADA); 
			} catch(AccountNotFoundException e) {
				Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (AbortException e) {
				Errores.ADMINISTRACION.ADMINISTRADOR_SIN_CENTRO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.NO_EXISTE_PRUEBA_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (DataFormatException e) {
				Errores.ADMINISTRACION.ERROR_HORARIOS_PRUEBA_CENTRO.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/eliminar", method = RequestMethod.DELETE)
	@ResponseBody
	ResponseEntity<String> eliminarCentroDeComunidad(@RequestParam Long id, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			servicioCentros.eliminarCentroComoAdministrador(id);
			return ResponseEntity.status(HttpStatus.OK).body(Globales.CENTROS.CENTRO_ELIMINADO); 
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/annadir-centro", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> annadirNuevoCentroAComunidad(@RequestBody Map<String, String> formulario, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			servicioCentros.annadirNuevoCentroAComunidad(formulario);
			return ResponseEntity.status(HttpStatus.OK).body(Globales.CENTROS.NUEVO_CENTRO_ANNADIDO); 
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/{idCentro}/actualizar-centro", method = RequestMethod.PATCH)
	@ResponseBody
	ResponseEntity<String> actualizarInformacionCentro(@RequestBody Map<String, String> formulario, @PathVariable(value="idCentro") Long idCentro, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				servicioCentros.actualizarInformacionCentro(idCentro, formulario);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.CENTROS.CENTRO_ACTUALIZADO); 
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.ERROR_IDENTIFICADOR_CENTRO.apply(String.valueOf(e.getMessage())).enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
				return null;
			}
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/{idCentro}/imagenes", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<List<ImagenCentro>> getImagenesDeCentro(@PathVariable Long idCentro, HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			try {
				return ResponseEntity.status(HttpStatus.OK).body(servicioCentros.getImagenesDeCentro(idCentro)); 
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.ERROR_IDENTIFICADOR_CENTRO.apply(idCentro.toString()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}			
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/administracion-comunidad/centros-comunidad/{idCentro}/imagenes", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<String> actualizarImagenDeCentro(@PathVariable Long idCentro, /*@RequestParam MultipartFile[] imagenCentro,*/ HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			List<MultipartFile> imagenesCentro = getListaImagenesDePeticion(peticion); 
			try {
				servicioCentros.actualizarImagenesDeCentro(idCentro, imagenesCentro);
				return ResponseEntity.status(HttpStatus.OK).body(Globales.CENTROS.NUEVO_CENTRO_ANNADIDO); 
			} catch (IOException e) {
				Errores.IMAGENES.ERROR_PROCESAR_IMAGEN.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			} catch (IllegalArgumentException e) {
				Errores.ADMINISTRACION.ERROR_IDENTIFICADOR_CENTRO.apply(idCentro.toString()).enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
				return null;
			}		
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	public List<MultipartFile> getListaImagenesDePeticion(HttpServletRequest peticion) {
		MultipartHttpServletRequest peticionMultipart = (MultipartHttpServletRequest) peticion;
		List<MultipartFile> listaFinal = new ArrayList<>();
	    Iterator<String> iteradorNombres = peticionMultipart.getFileNames();
	    while (iteradorNombres.hasNext()) {
	    	listaFinal.addAll(peticionMultipart.getFiles(iteradorNombres.next()));
	    }
	    return listaFinal;
	}
	
}
