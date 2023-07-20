package es.project.Pandemic.Controladores;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Seccion;
import es.project.Pandemic.Servicios.ServicioSecciones;

@RestController
public class ControladorSeccionesAcciones {
	
	@Autowired
	ServicioSecciones servicioSecciones;
	
	@Autowired
	ConfiguracionJWT JWT;
	
	@RequestMapping(value = "/Pandemic/acciones/{idSeccion}", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getAccionesSeccion(HttpServletRequest peticion, HttpServletResponse respuesta, @PathVariable(value="idSeccion") long idSeccion) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			return ResponseEntity.status(HttpStatus.OK).body(servicioSecciones.getAccionesSeccion(idSeccion, tokenDecodificado.getSubject())) ;
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/secciones", method = RequestMethod.GET)
	public ResponseEntity<List<Seccion>> getTodasSecciones(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			return ResponseEntity.status(HttpStatus.OK).body(servicioSecciones.getTodasLasSecciones()) ;
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/secciones-menu", method = RequestMethod.GET)
	public ResponseEntity<List<Seccion>> getSeccionesMenu(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		if (token != null) {
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			return ResponseEntity.status(HttpStatus.OK).body(servicioSecciones.getSeccionesMenuParaUsuario(tokenDecodificado.getSubject())) ;
		}
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		return null;
	}
	
	@RequestMapping(value = "/Pandemic/delay", method = RequestMethod.GET)
	public ResponseEntity<List<Seccion>> getDelay(HttpServletRequest peticion, HttpServletResponse respuesta) {
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(servicioSecciones.getTodasLasSecciones()) ;
	}
	
}
