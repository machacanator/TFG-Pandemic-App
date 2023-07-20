package es.project.Pandemic.Filtros;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.project.Pandemic.Configuracion.ConfiguracionJWT;
import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Excepciones.ManejadorExcepciones;
import es.project.Pandemic.Repositorios.RepositorioUsuarios;
import es.project.Pandemic.Servicios.ServicioUsuarios;

@Component
public class FiltroTokens extends OncePerRequestFilter {
	
	@Autowired
	ServicioUsuarios servicioUsuarios;
	
	@Autowired
	ConfiguracionJWT JWT;
	
	public FiltroTokens(ConfiguracionJWT JWT, ServicioUsuarios servicioUsuarios ) {
		this.JWT = JWT;
		this.servicioUsuarios = servicioUsuarios;
	}
	
	private boolean isRequiredAuthentication(String urlPeticion) {
		return !Globales.URL_PUBLICAS.stream().anyMatch(url -> urlPeticion.contains(url));
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain filtroCadena) throws IOException, ServletException {
		String token = peticion.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
		
		try{
			if(token == null && isRequiredAuthentication(peticion.getRequestURL().toString())) {
				throw new JWTDecodeException("");
			} else if (token == null) {
				filtroCadena.doFilter(peticion, respuesta);
				return;
			}
			
			DecodedJWT tokenDecodificado = JWT.decodificarToken(token);
			
			String documentoIdentidad = tokenDecodificado.getSubject();
			
			UserDetails informacionUsuario = servicioUsuarios.loadUserByUsername(documentoIdentidad); 
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					informacionUsuario.getUsername(), informacionUsuario.getPassword(), informacionUsuario.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filtroCadena.doFilter(peticion, respuesta);
		} catch (JWTDecodeException ex) {
			Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		} catch (SignatureVerificationException ex) {
			Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		} catch (AuthenticationException ex) {
			Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		} catch (TokenExpiredException ex) {
			Errores.AUTENTICACION.TOKEN_EXPIRADO.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
		}
	}
}


/*@Component
public class FiltroTokens extends UsernamePasswordAuthenticationFilter   {
	
	private final String PATH_AUTENTICACION = "/Pandemic/refrescarToken";
	private final String PARAMETRO_NOMBRE_USUARIO = "documentoIdentidad";
	private final String PARAMETRO_CONTRASENNA_USUARIO = "contrasenna";
	
	
	// We use auth manager to validate the user credentials
	private AuthenticationManager authManager;
	
	private final ConfiguracionJWT jwtConfig;
	
	private ServicioUsuarios servicioUsuarios;
	
	private ManejadorExcepciones manejadorExcepciones;
    
	public FiltroTokens(AuthenticationManager authManager, ConfiguracionJWT jwtConfig, ServicioUsuarios servicioUsuarios, ManejadorExcepciones manejadorExcepciones) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		this.servicioUsuarios = servicioUsuarios;
		this.manejadorExcepciones = manejadorExcepciones;
		
		// By default, UsernamePasswordAuthenticationFilter listens to "/login" path. 
		// In our case, we use "/auth". So, we need to override the defaults.
		//this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
		setFilterProcessesUrl(PATH_AUTENTICACION);
		setUsernameParameter(PARAMETRO_NOMBRE_USUARIO);
		setPasswordParameter(PARAMETRO_CONTRASENNA_USUARIO);
		setAuthenticationManager(authManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
			String token = request.getHeader(Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO);
			JWTVerifier verificador = JWT.require(jwtConfig.getAlgoritmo()).build();
			DecodedJWT tokenDecodificado = verificador.verify(token);
			String documentoIdentidad = tokenDecodificado.getSubject();
			// 1. Get credentials from request
			UserDetails informacionUsuario = servicioUsuarios.loadUserByUsername(documentoIdentidad); 
			//Usuario creds = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			
			// 2. Create auth object (contains credentials) which will be used by auth manager
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					informacionUsuario.getUsername(), informacionUsuario.getPassword(), informacionUsuario.getAuthorities());
			
			// 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
			return authManager.authenticate(authToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain cadena, Authentication autentificacion) throws IOException, ServletException {
		Usuario usuario = (Usuario) autentificacion.getPrincipal();
		
		String tokenAcceso = jwtConfig.crearTokenAcceso(usuario, peticion);
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
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(manejadorExcepciones.manejarExcepcionNoAutorizado(failed).toString());
	}
	
	// Upon successful authentication, generate a token.
	// The 'auth' passed to successfulAuthentication() is the current authenticated user.
	//@Override
	//protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
		
		Long now = System.currentTimeMillis();
		String token = Jwt.builder()
			.setSubject(auth.getName())	
			// Convert to list of strings. 
			// This is important because it affects the way we get them back in the Gateway.
			.claim("authorities", auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.setIssuedAt(new Date(now))
			.setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
			.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
			.compact();
		
		// Add token to header
		response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
	}
}*/
