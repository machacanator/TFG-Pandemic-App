package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notificacion")
public class Notificacion implements Comparable<Notificacion>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	
	@Column(nullable = false)
	private Date fecha;
	
	@Column(nullable = false)
	private boolean visualizada;
	
	@Column(nullable = false, length = 45)
	private String codigo;
	
	@Column(name = "usuario", nullable = false)
	private long usuario;
	
	@Column(name = "idreferencia", nullable = false)
	private long idReferencia;
	
	/*@Transient
	@Autowired
	ServicioCitas servicioCitas;
	
	@Transient
	@Autowired
	ServicioPruebasComplementarias servicioPruebasComplementarias;*/

	public Notificacion() {}
	
	public Notificacion(long id, Date fecha, boolean visualizada, String codigo, long idUsuario, long idReferencia) {
		this.id = id;
		this.fecha = fecha;
		this.visualizada = visualizada;
		this.codigo = codigo;
		this.usuario = idUsuario;
		this.idReferencia = idReferencia;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean getVisualizada() {
		return visualizada;
	}

	public void setVisualizada(boolean visualizada) {
		this.visualizada = visualizada;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public long getIdReferencia() {
		return idReferencia;
	}
	
	public long getUsuario() {
		return usuario;
	}

	public void setUsuario(long usuario) {
		this.usuario = usuario;
	}
	
	// El objeto puede ser una cita o una prueba complementaria
	/*public void setReferencia(Object referencia) {
		this.referencia = referencia;
	}*/
	
	// Recibe la id y obtiene el objeto asociado a la id basandose en el valor del codigo de la notificación
	public void setIdReferencia(long idReferencia) {
		/*if (codigo.contains("cita")) {
			this.referencia = servicioCitas.getCitaPorId(idReferencia);
		} else if (codigo.contains("prueba")) {
			this.referencia = servicioPruebasComplementarias.getPruebaComplemetariaPorId(idReferencia);
		} else {
			this.referencia = null;
		}*/
		this.idReferencia = idReferencia;
	}
	
	@Override
	public int compareTo(Notificacion notificacionAComparar) {
		return this.getFecha().before(notificacionAComparar.getFecha()) ? 1 : -1;
	}
	
}
