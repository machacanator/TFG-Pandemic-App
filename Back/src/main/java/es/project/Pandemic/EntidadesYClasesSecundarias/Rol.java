package es.project.Pandemic.EntidadesYClasesSecundarias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rol")
public class Rol implements Comparable<Rol>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 45)
	private String nombre;

	public Rol() {}
	
	public Rol(long id, String nombre) {
		this.id = id;
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return nombre;
	}
	
	@Override
	public int compareTo(Rol rolAComparar) {
		return this.getNombre().compareTo(rolAComparar.getNombre());
	}
	
}
