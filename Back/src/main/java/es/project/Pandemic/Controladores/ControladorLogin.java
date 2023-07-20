package es.project.Pandemic.Controladores;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Servicios.ServicioSecciones;

@RestController
public class ControladorLogin {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	ConfiguracionJWT JWT;
	
	@Autowired
	ServicioSecciones servicioSecciones;
	
	/*@RequestMapping(value = "/Pandemic/iniciarSesion", method = RequestMethod.POST)
	public void iniciarSesion() {}*/
	
	@RequestMapping(value = "/Pandemic/iniciarSesion", method = RequestMethod.POST)
	public ResponseEntity<UserDetails>/*ResponseEntity<String>*/ login(HttpServletRequest peticion, HttpServletResponse respuesta) throws IOException {
        try {
        	String documentoIdentidad = peticion.getParameter(Globales.LOGIN.PARAMETRO_NOMBRE_USUARIO);
    		String contrasenna = peticion.getParameter(Globales.LOGIN.PARAMETRO_CONTRASENNA_USUARIO);
        	Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		documentoIdentidad, contrasenna
                    )
                );

            Usuario user = (Usuario) authenticate.getPrincipal();

            return ResponseEntity.ok()
                .header(
                    Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO,
                    JWT.crearTokenAcceso(user.getDocumentoIdentidad(), user.getRolesAsString(), peticion)
                )
                .body(user/*Globales.LOGIN.LOGIN_CORRECTO*/);
        } catch (BadCredentialsException ex) {
        	Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
        }
		return null;
    }
	
	@RequestMapping(value = "/Pandemic/refrescarToken", method = RequestMethod.POST)
	public ResponseEntity<String> refrescarToken(HttpServletRequest peticion, HttpServletResponse respuesta) {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		return ResponseEntity.ok()
                .header(
                        Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO,
                        JWT.refrescarToken(token, peticion)
                    )
                .body(Globales.TOKEN.REFRESCO_TOKEN_CORRECTO);
	}
	
	@RequestMapping(value = "/Pandemic/home", method = RequestMethod.GET)
	public ResponseEntity<String> home() {
		return ResponseEntity.status(HttpStatus.OK).body("Se muestra la pagina de inicio correctamente");
	}
	
	@RequestMapping(value = "/Pandemic/onlyAdmins", method = RequestMethod.GET)
	public ResponseEntity<String> onlyAdmins() {
		return ResponseEntity.status(HttpStatus.OK).body("Se muestra la pagina de inicio correctamente");
	}
	
}
