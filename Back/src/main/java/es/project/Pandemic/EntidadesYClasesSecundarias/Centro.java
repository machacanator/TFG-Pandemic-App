package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.ArrayList;
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
import javax.persistence.Transient;

@Entity
@Table(name = "centromedico")
public class Centro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(nullable = false)
	private String direccion;
	
	@Column(name = "codigopostal", nullable = false)
	private String codigoPostal;
	
	@Column(nullable = false)
	private String municipio;
	
	@Column(name = "cartapresentacion")
	private String cartaPresentacion;
	
	private String historia;
	private String mision;
	
	@Transient
	private List<HorariosPrueba> horarios;
	
	public Centro() {}
	
	public Centro(long id, String nombre, String direccion, String codigoPostal, String municipio,
			String cartaPresentacion, String historia, String mision) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
		this.municipio = municipio;
		this.cartaPresentacion = cartaPresentacion;
		this.historia = historia;
		this.mision = mision;
	}

	public Centro(long id, String nombre, String direccion, String codigoPostal, String municipio,
			String cartaPresentacion, String historia, String mision, List<HorariosPrueba> horarios) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
		this.municipio = municipio;
		this.cartaPresentacion = cartaPresentacion;
		this.historia = historia;
		this.mision = mision;
		this.horarios = horarios;
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getCartaPresentacion() {
		return cartaPresentacion;
	}

	public void setCartaPresentacion(String cartaPresentacion) {
		this.cartaPresentacion = cartaPresentacion;
	}

	public String getHistoria() {
		return historia;
	}

	public void setHistoria(String historia) {
		this.historia = historia;
	}

	public String getMision() {
		return mision;
	}

	public void setMision(String mision) {
		this.mision = mision;
	}

	public List<HorariosPrueba> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<HorariosPrueba> horarios) {
		this.horarios = horarios;
	}
	
}
