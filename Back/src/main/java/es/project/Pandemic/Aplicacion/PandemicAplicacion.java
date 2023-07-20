package es.project.Pandemic.Aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {
		"es.project.Pandemic.Controladores",
		"es.project.Pandemic.Configuracion",
		"es.project.Pandemic.Excepciones",
		"es.project.Pandemic.Servicios",
		"es.project.Pandemic.Filtros",
})
@EnableSwagger2
@EnableWebMvc
@EnableScheduling
@EnableJpaRepositories(basePackages = {
		"es.project.Pandemic.Repositorios",
})
@EntityScan("es.project.Pandemic.EntidadesYClasesSecundarias")
public class PandemicAplicacion {

	public static void main(String[] args) {
		SpringApplication.run(PandemicAplicacion.class, args);
	}
	
	/*@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/Pandemic/**").allowedOrigins("http://localhost:4200").allowedMethods("*").allowedHeaders("*");
			}
		};
	}*/

}
