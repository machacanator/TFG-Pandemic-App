package es.project.Pandemic.Configuracion;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.EntidadesYClasesSecundarias.Rol;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;
import es.project.Pandemic.Excepciones.ExcepcionAutenticacion;
import es.project.Pandemic.Servicios.ServicioUsuarios;

@Component("ConfiguracionJWT")
public class ConfiguracionJWT {
	
	@Autowired
	ServicioUsuarios servicioUsuarios;
	
	private static final String PARAMETRO_ROLES_TOKEN = "roles";
	private static final String CLAVE_SECRETA_JWT = "Pandemic2022";
	private static final long TIEMPO_EXPIRACION_TOKEN_MILISEGUNDOS = (long) (60*60*1000); //1 hora
	private static final Algorithm algoritmo = Algorithm.HMAC256(CLAVE_SECRETA_JWT/*.getBytes()*/);
	
	/*public static String crearTokenAcceso(Usuario usuario, HttpServletRequest peticion) {
		return JWT.create()
			.withSubject(usuario.getDocumentoIdentidad())
			.withExpiresAt(new Date(System.currentTimeMillis()+TIEMPO_EXPIRACION_TOKEN_MILISEGUNDOS))
			.withIssuer(peticion.getRequestURL().toString())
			.withClaim(PARAMETRO_ROLES_TOKEN, usuario.getRoles().stream().map(GrantedAuthority :: getAuthority).collect(Collectors.toList()))
			.sign(algoritmo);
	}*/
	
	public String crearTokenAcceso(String documentoIdentidad, List<String> roles, HttpServletRequest peticion) {
		return JWT.create()
			.withSubject(documentoIdentidad)
			.withExpiresAt(new Date(System.currentTimeMillis()+TIEMPO_EXPIRACION_TOKEN_MILISEGUNDOS))
			.withIssuer(peticion.getRequestURL().toString())
			.withClaim(PARAMETRO_ROLES_TOKEN, roles)
			.sign(algoritmo);
	}
	
	/*public String crearTokenRefresco(Usuario usuario, HttpServletRequest peticion) {
		return JWT.create()
				.withSubject(usuario.getDocumentoIdentidad())
				.withExpiresAt(new Date(System.currentTimeMillis()+TIEMPO_EXPIRACION_TOKEN_MILISEGUNDOS))
				.withIssuer(peticion.getRequestURL().toString())
				.sign(algoritmo);
	}*/
	
	public String refrescarToken(String token, HttpServletRequest peticion) throws AuthenticationException {
		DecodedJWT tokenDecodificado = decodificarToken(token);
		String documentoIdentidad = tokenDecodificado.getSubject();
		//List<String> rolesString = tokenDecodificado.getClaim(PARAMETRO_ROLES_TOKEN).asList(String.class);
		//List<SimpleGrantedAuthority> roles = getRoles(rolesString);
		Usuario informacionUsuario = servicioUsuarios.getUsuarioPorDocumentoIdentidad(documentoIdentidad);
		return crearTokenAcceso(informacionUsuario.getUsername(), informacionUsuario.getRolesAsString(), peticion);
	}
	
	/*public List<SimpleGrantedAuthority> getRoles(List<String> listaRolesString) {
		List<SimpleGrantedAuthority> resultado = new ArrayList();
		listaRolesString.forEach(rol -> resultado.add(new SimpleGrantedAuthority(rol)));
		return resultado;
	}*/
	
	public List<SimpleGrantedAuthority> getRoles(Collection<GrantedAuthority> listaRolesString) {
		List<SimpleGrantedAuthority> resultado = new ArrayList();
		listaRolesString.forEach(rol -> resultado.add(new SimpleGrantedAuthority(rol.getAuthority())));
		return resultado;
	}
	
	public DecodedJWT decodificarToken(String token) {
		JWTVerifier verificador = JWT.require(algoritmo).build();
		return verificador.verify(token);
	}
	
	public Algorithm getAlgoritmo() {
		return algoritmo;
	}
	
}
