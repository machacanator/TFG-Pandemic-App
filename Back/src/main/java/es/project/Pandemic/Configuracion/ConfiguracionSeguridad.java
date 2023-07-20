package es.project.Pandemic.Configuracion;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.project.Pandemic.Constantes.Errores;
import es.project.Pandemic.Constantes.Globales;
import es.project.Pandemic.Excepciones.ManejadorExcepciones;
import es.project.Pandemic.Filtros.FiltroTokens;
import es.project.Pandemic.Servicios.ServicioUsuarios;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad{
	
	public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	    @Override
	    public void handle(HttpServletRequest request, HttpServletResponse respuesta, AccessDeniedException exc) throws IOException {
	    	Errores.AUTENTICACION.ERROR_AUTENTICACION.enviar(HttpServletResponse.SC_UNAUTHORIZED, respuesta);
	    }
	}
	
	@Autowired
	ServicioUsuarios servicioUsuarios;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager, ConfiguracionJWT configuracionJWT, ManejadorExcepciones manejadorExcepciones) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(new FiltroTokens(/*authManager,*/ configuracionJWT, servicioUsuarios), BasicAuthenticationFilter.class);
		http
			.cors(Customizer.withDefaults())
			.authorizeRequests()
			.antMatchers(Globales.URL_PUBLICAS.toArray(new String[Globales.URL_PUBLICAS.size()])).permitAll()
			.antMatchers(HttpMethod.OPTIONS, "/", "/**").permitAll()
			.antMatchers("/Pandemic/onlyAdmins").hasAuthority("Administrador de Hospital")
			.antMatchers("/Pandemic/administracion-centro").hasAnyAuthority("Administrador de Hospital", "Administrador Comunidad")
			.antMatchers("/Pandemic/administracion-centro/pruebas-centro").hasAnyAuthority("Administrador de Hospital")
			.antMatchers("/Pandemic/administracion-comunidad/centros-comunidad").hasAnyAuthority("Administrador Comunidad")
			.antMatchers("/Pandemic/persona").hasAnyAuthority("Enfermero")
			.antMatchers("/Pandemic/citas/diario").hasAnyAuthority("Enfermero")
			.antMatchers("/Pandemic/citas/asignarCita").hasAnyAuthority("Enfermero")
			.antMatchers("/Pandemic/citas/gestionarCita").hasAnyAuthority("Enfermero")
			.antMatchers("/Pandemic/administracion-pruebas").hasAnyAuthority("Administrador de Hospital")
			.antMatchers("/Pandemic/administracion-plantilla").hasAnyAuthority("Administrador de Hospital")
			.antMatchers("/Pandemic/administracion-comunidad").hasAnyAuthority("Administrador Comunidad")
			.antMatchers("/Pandemic/estadisticas").hasAnyAuthority("Enfermero", "Administrador de Hospital", "Administrador Comunidad")
			.antMatchers("/Pandemic/estadisticas/centro").hasAnyAuthority("Administrador de Hospital", "Administrador Comunidad")
			.antMatchers("/Pandemic/estadisticas/comunidad").hasAuthority("Administrador Comunidad")
			.anyRequest().authenticated()
			.and()
			//.addFilter(new FiltroAutenticacion(authManager, configuracionJWT, manejadorExcepciones))
			.exceptionHandling()
	        .accessDeniedHandler(accessDeniedHandler())
	        .and()
			.authenticationManager(authManager)
			.httpBasic();
			/*.authorizeRequests()
			.anyRequest().permitAll()
			//.antMatchers("/loginPublico").permitAll()
			.and()
			.addFilter(new FiltroAutenticacion(authManager))
			.authenticationManager(authManager)
			.httpBasic();*/
		return http.build();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
	   return new CustomAccessDeniedHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder codificadorContrasenna, ServicioUsuarios servicioUsuarios) throws Exception {
		// Usar solo cuando queramos almacenar una contraseña cuyo codigo desconocemos. 
		// System.out.println(codificadorContrasenna.encode("Pandemic2022!"));
		return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .userDetailsService(servicioUsuarios)
	            .passwordEncoder(codificadorContrasenna)
	            .and()
	            .build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cc = new CorsConfiguration();
			cc.setAllowedHeaders(Arrays.asList("*"));
			//cc.setAllowedHeaders(Arrays.asList("Origin,Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers","Authorization"));
	        cc.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", Globales.TOKEN.PARAMETRO_HEADER_TOKEN_ACCESO));                
		cc.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		//cc.setAllowedMethods(Arrays.asList("*"));
		cc.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT","PATCH", "DELETE"));
            //cc.addAllowedOrigin("*");
            //cc.setMaxAge(Duration.ZERO);
            cc.setAllowCredentials(Boolean.TRUE);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cc);
		return source;
	 }
	
}
