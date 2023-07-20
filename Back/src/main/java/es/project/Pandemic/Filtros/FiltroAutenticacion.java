package es.project.Pandemic.Filtros;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Excepciones.ExcepcionAutenticacion;
import es.project.Pandemic.Excepciones.ManejadorExcepciones;

public class FiltroAutenticacion extends UsernamePasswordAuthenticationFilter{
	
	private final String PATH_AUTENTICACION = "/Pandemic/refrescarToken";
	private final String PARAMETRO_NOMBRE_USUARIO = "documentoIdentidad";
	private final String PARAMETRO_CONTRASENNA_USUARIO = "contrasenna";
	
	@Autowired
	ConfiguracionJWT JWT;
	@Autowired
	ManejadorExcepciones manejadorExcepciones;
	
	public FiltroAutenticacion(AuthenticationManager authenticationManager, ConfiguracionJWT JWT, ManejadorExcepciones manejadorExcepciones) {
		this.JWT = JWT;
		this.manejadorExcepciones = manejadorExcepciones;
		setFilterProcessesUrl(PATH_AUTENTICACION);
		setUsernameParameter(PARAMETRO_NOMBRE_USUARIO);
		setPasswordParameter(PARAMETRO_CONTRASENNA_USUARIO);
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest peticion, HttpServletResponse respuesta) throws AuthenticationException {
		String documentoIdentidad = obtainUsername(peticion);
		String contraseña = obtainPassword(peticion);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(documentoIdentidad, contraseña);
		return this.getAuthenticationManager().authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain cadena, Authentication autentificacion) throws IOException, ServletException {
		Usuario usuario = (Usuario) autentificacion.getPrincipal();
		
		String tokenAcceso = JWT.crearTokenAcceso(usuario.getDocumentoIdentidad(), usuario.getRolesAsString(), peticion);
		//String tokenRefresco = JWT.crearTokenRefresco(usuario, peticion);
		respuesta.setHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO, tokenAcceso);
		//respuesta.setHeader(PARAMETRO_HEADER_REFRESCAR_TOKEN, tokenRefresco);
		if (peticion.getMethod().equalsIgnoreCase("POST") && peticion.getRequestURL().toString().contains(PATH_AUTENTICACION)) {			
			respuesta.setContentLength(Globales.LOGIN.LOGIN_CORRECTO.length());
			respuesta.getWriter().write(Globales.LOGIN.LOGIN_CORRECTO);
		}
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, response);
	}
}
