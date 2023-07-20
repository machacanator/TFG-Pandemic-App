package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "seccion")
public class Seccion implements Comparable<Seccion> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 45)
	private String nombre;
	
	@Column(name="accesolibre", nullable = false)
	private boolean accesoLibre;
	
	@Column(name="mostrarenmenu", nullable = false)
	private boolean mostrarMenuPrincipal;
	
	@OneToMany
	@JoinTable(
			name="seccion",
			joinColumns = @JoinColumn(name="seccionpadre"),
			inverseJoinColumns = @JoinColumn(name="id")
	)
	@OrderBy("id")
	private List<Seccion> subSecciones;


	public Seccion() {
	}

	public Seccion(long id, String nombre, boolean accesoLibre, boolean mostrarMenuPrincipal, List<Seccion> subSecciones) {
		this.id = id;
		this.nombre = nombre;
		this.accesoLibre = accesoLibre;
		this.mostrarMenuPrincipal = mostrarMenuPrincipal;
		this.subSecciones = subSecciones;
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
	
	public boolean getAccesoLibre() {
		return accesoLibre;
	}

	public void setAccesoLibre(boolean accesoLibre) {
		this.accesoLibre = accesoLibre;
	}
	
	public boolean getMostrarMenuPrincipal() {
		return mostrarMenuPrincipal;
	}

	public void setMenuPrincipal(boolean mostrarMenuPrincipal) {
		this.mostrarMenuPrincipal = mostrarMenuPrincipal;
	}
	
	public List<Seccion> getSubSecciones() {
		return subSecciones;
	}
	
	public void setSubSecciones(List<Seccion> subSecciones) {
		this.subSecciones = subSecciones;
	}
	
	@Override
	public int compareTo(Seccion seccion) {
		return this.id > seccion.id ? 1 : this.id < seccion.id ? -1 : 0;
	}

}
