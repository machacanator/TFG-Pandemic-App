package es.project.Pandemic.EntidadesYClasesSecundarias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pruebacomplementaria")
public class PruebaComplementaria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(name = "duracion", nullable = false)
	private long duracionPrueba;
	
	@Column(name = "separacion", nullable = false)
	private long separacionSiguientePrueba;
	
	@Column(nullable = false)
	private String descripcion;
	
	@Column(nullable = false)
	private String avisos;
	
	@Column(nullable = false)
	private String recomendaciones;
	
	public PruebaComplementaria() {}

	public PruebaComplementaria(long id, String nombre, long duracionPrueba, String descripcion, String avisos,
			String recomendaciones, long separacion) {
		this.id = id;
		this.nombre = nombre;
		this.duracionPrueba = duracionPrueba;
		this.separacionSiguientePrueba = separacion;
		this.descripcion = descripcion;
		this.avisos = avisos;
		this.recomendaciones = recomendaciones;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getDuracionPrueba() {
		return duracionPrueba;
	}

	public void setDuracionPrueba(long duracionPrueba) {
		this.duracionPrueba = duracionPrueba;
	}

	public long getSeparacionSiguientePrueba() {
		return separacionSiguientePrueba;
	}

	public void setSeparacionSiguientePrueba(long separacionSiguientePrueba) {
		this.separacionSiguientePrueba = separacionSiguientePrueba;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAvisos() {
		return avisos;
	}

	public void setAvisos(String avisos) {
		this.avisos = avisos;
	}

	public String getRecomendaciones() {
		return recomendaciones;
	}

	public void setRecomendaciones(String recomendaciones) {
		this.recomendaciones = recomendaciones;
	}
}
