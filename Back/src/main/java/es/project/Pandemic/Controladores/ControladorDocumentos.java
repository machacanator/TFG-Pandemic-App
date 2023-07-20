package es.project.Pandemic.Controladores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.project.Pandemic.Constantes.Errores;

@RestController
public class ControladorDocumentos {
	

	@RequestMapping(value = "/Pandemic/documentos/aviso-legal", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Resource> getAvisoLegal(HttpServletResponse respuesta) {
		try {
			 ClassPathResource direccion = new ClassPathResource("static/AVISO_LEGAL_POLITICAS_PRIVACIDAD.pdf");
			 Resource urlFichero = new UrlResource(direccion.getURI());
			 HttpHeaders httpHeaders = new HttpHeaders();
			 httpHeaders.add("nombre_documento", "AVISO_LEGAL_POLITICAS_PRIVACIDAD");
			 httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;nombre_documento="+urlFichero.getFilename());
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(Path.of(direccion.getURI())))).headers(httpHeaders).body(urlFichero);
		} catch (IOException e) {
			Errores.DOCUMENTOS.ERROR_TERMINOS_CONDICIONES.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
			return null;
		} 
	}
	
	@RequestMapping(value = "/Pandemic/documentos/terminos-y-condiciones", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Resource> getTerminosYCondiciones(HttpServletResponse respuesta) {
		try {
			 ClassPathResource direccion = new ClassPathResource("static/TERMINOS_CONDICIONES.pdf");
			 Resource urlFichero = new UrlResource(direccion.getURI());
			 HttpHeaders httpHeaders = new HttpHeaders();
			 httpHeaders.add("nombre_documento", "TERMINOS_CONDICIONES");
			 httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;nombre_documento="+urlFichero.getFilename());
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(Path.of(direccion.getURI())))).headers(httpHeaders).body(urlFichero);
		} catch (IOException e) {
			Errores.DOCUMENTOS.ERROR_TERMINOS_CONDICIONES.enviar(HttpServletResponse.SC_EXPECTATION_FAILED, respuesta);
			return null;
		} 
	}
}
