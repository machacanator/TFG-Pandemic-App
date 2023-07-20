package es.project.Pandemic.Excepciones;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.project.Pandemic.Constantes.Errores;

@RestControllerAdvice
public class ManejadorExcepciones {
	private static final String PARAMETRO_JSON_CODIGO_ERROR = "codigo";
	private static final String PARAMETRO_JSON_MENSAJE_ERROR = "mensaje";
	
	private static String getSimpleName(Exception e) {
		return e.getClass().getSimpleName();
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JSONObject manejarExcepcionUsuarioNoEncontrado(UsernameNotFoundException excepcion) {
		return new JSONObject("Usuario no encontrado");
	}
	
	  /*@ResponseStatus(HttpStatus.CONFLICT)
	  @ExceptionHandler(DuplicateIdException.class)
	  public MensajeError handleDuplicateIdException(DuplicateIdException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }
	  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(InvalidMenuGroupCountException.class)
	  public MensajeError handleInvalidMenuGroupCountException(InvalidMenuGroupCountException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }
	  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(InvalidMenuGroupIdException.class)
	  public MensajeError handleInvalidMenuGroupIdException(InvalidMenuGroupIdException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }*/
	  
	  /*@ExceptionHandler(HttpStatusCodeException.class) 
	  public ResponseEntity handleHttpStatusCodeException(HttpStatusCodeException e) {
	    return ResponseEntity.status(e.getStatusCode()).build();
	  }
	  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(IllegalArgumentException.class)
	  public MensajeError handleIllegalArgumentException(IllegalArgumentException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }
	  
	  /*@ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(value = {CanNotOpenShopException.class, CanNotCloseShopException.class})
	  public MensajeError handleCannotShopException(RuntimeException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }

	  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(IssuedCouponExistException.class)
	  public MensajeError handleIssuedCouponExistException(IssuedCouponExistException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }
	  
	  @ResponseStatus(HttpStatus.CONFLICT)
	  @ExceptionHandler(DuplicateItemException.class)
	  public MensajeError handleDuplicatedItemException(DuplicateItemException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }
	  
	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	  @ExceptionHandler(MockPayException.class)
	  public MensajeError handleMockPayException(MockPayException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }
	  
	  @ResponseStatus(HttpStatus.UNAUTHORIZED)
	  @ExceptionHandler(IdDeletedException.class)
	  public MensajeError handleIdDeletedException(IdDeletedException e) {
	    return new MensajeError(e.getLocalizedMessage(), getSimpleName(e));
	  }*/
	
}
