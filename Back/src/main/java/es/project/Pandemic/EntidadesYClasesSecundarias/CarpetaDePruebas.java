package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "carpetadepruebas")
public class CarpetaDePruebas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	public CarpetaDePruebas() {}
	
	public CarpetaDePruebas(Object[] objeto) {
		this.setId(((BigInteger) objeto[0]).intValue());
		this.setNombre((String) objeto[1]);
	}

	public CarpetaDePruebas(long id, String nombre) {
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
}
