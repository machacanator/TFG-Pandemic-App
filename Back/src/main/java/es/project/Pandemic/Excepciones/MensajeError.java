package es.project.Pandemic.Excepciones;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import es.project.Pandemic.Constantes.Errores;

public class MensajeError {
	
	private String codigo;
	private String mensaje;
	
	public MensajeError(String codigo, String mensaje) {
		super();
		this.codigo = codigo;
		this.mensaje = mensaje;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public void enviar(int estadoRespuesta, HttpServletResponse respuesta) {
		try {
			JSONObject json = new JSONObject();
			json.put(Errores.PARAMETROS.PARAMETRO_CODIGO, getCodigo());
			json.put(Errores.PARAMETROS.PARAMETRO_MENSAJE, getMensaje());
			respuesta.setContentType("application/json");
			respuesta.setCharacterEncoding("UTF-8");
			respuesta.setStatus(estadoRespuesta);
			respuesta.getWriter().write(json.toString());
		} catch (IOException e) {
			Errores.RESPUESTA.ERROR_CUERPO_RESPUESTA.enviar(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respuesta);
		}
	}
	
}
