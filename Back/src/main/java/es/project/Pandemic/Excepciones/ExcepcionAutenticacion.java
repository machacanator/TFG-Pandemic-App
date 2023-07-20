package es.project.Pandemic.Excepciones;

import org.springframework.security.core.AuthenticationException;

public class ExcepcionAutenticacion extends AuthenticationException{

	public ExcepcionAutenticacion() {
		super("");
	}
}
