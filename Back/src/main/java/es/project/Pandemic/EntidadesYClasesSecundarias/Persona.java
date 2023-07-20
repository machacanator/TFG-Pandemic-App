package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persona")
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "nombre")//@Column(nullable = false)
	private String nombre;
	
	@Column(name = "apellidos")//@Column(nullable = false)
	private String apellidos;
	
	@Column(name = "documentoidentidad")//@Column(nullable = false, length = 9, unique = true)
	private String documentoIdentidad;
	
	@Column(name = "numeroseguridadsocial")//@Column(nullable = false, length = 9, unique = true)
	private String numeroSeguridadSocial;
	
	@Column(name = "usuario")//@Column(nullable = false)
	private long usuario;
	
	@Column(name = "fechanacimiento")//@Column(nullable = false)
	private Date fechaNacimiento;
	
	@Column(name = "direccion")//@Column(nullable = false)
	private String direccion;
	
	@Column(name = "codigopostal")//@Column(nullable = false)
	private String codigoPostal;
	
	@Column(name = "telefono")//@Column(nullable = false)
	private String telefono;
	
	@Column(name = "telefonoalternativo")//@Column(nullable = false)
	private String telefonoAlternativo;
	
	@Column(name = "centroreferencia")//@Column(nullable = false)
	private long centroReferencia;
	
	public Persona() {}

	public Persona(long id, String nombre, String apellidos, String documentoIdentidad, String numeroSeguridadSocial, long usuario,
			Date fechaNacimiento, String direccion, String codigoPostal, String telefono, String telefonoAlternativo, long centroReferencia) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.documentoIdentidad = documentoIdentidad;
		this.numeroSeguridadSocial = numeroSeguridadSocial;
		this.usuario = usuario;
		this.fechaNacimiento = fechaNacimiento;
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
		this.telefono = telefono;
		this.telefonoAlternativo = telefonoAlternativo;
		this.centroReferencia = centroReferencia;
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	public void setDocumentoIdentidad(String documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}
	
	public String getNumeroSeguridadSocial() {
		return numeroSeguridadSocial;
	}

	public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
		this.numeroSeguridadSocial = numeroSeguridadSocial;
	}

	public long getUsuario() {
		return usuario;
	}

	public void setUsuario(long usuario) {
		this.usuario = usuario;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefonoAlternativo() {
		return telefonoAlternativo;
	}

	public void setTelefonoAlternativo(String telefonoAlternativo) {
		this.telefonoAlternativo = telefonoAlternativo;
	}

	public long getCentroReferencia() {
		return centroReferencia;
	}

	public void setCentroReferencia(long centroReferencia) {
		this.centroReferencia = centroReferencia;
	}
	
	public int getEdad() {
		Calendar calendarioHoy = Calendar.getInstance();
		Calendar calendarioFechaNacimiento = Calendar.getInstance();
		calendarioHoy.setTime(new Date());
		calendarioFechaNacimiento.setTime(fechaNacimiento);
		return calendarioHoy.get(Calendar.YEAR) - calendarioFechaNacimiento.get(Calendar.YEAR);
	}
	
}
