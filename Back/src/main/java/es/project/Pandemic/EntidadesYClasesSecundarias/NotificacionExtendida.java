package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.Date;

public class NotificacionExtendida<T> extends Notificacion {
	
	private T referencia;

	public NotificacionExtendida(long id, Date fecha, boolean visualizada, String codigo, long idUsuario, long idReferencia, T referencia) {
		super(id, fecha, visualizada, codigo, idUsuario, idReferencia);
		this.referencia = referencia;
	}
	
	public NotificacionExtendida(Notificacion notificacion, T referencia) {
		this.setId(notificacion.getId());
		this.setFecha(notificacion.getFecha());
		this.setVisualizada(notificacion.getVisualizada());
		this.setCodigo(notificacion.getCodigo());
		this.setIdReferencia(notificacion.getIdReferencia());
		this.referencia = referencia;
	}

	public T getReferencia() {
		return this.referencia;
	}

	public void setReferencia(T referencia) {
		this.referencia = referencia;
	}
}
