package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.joda.time.DateTime;

import es.project.Pandemic.Constantes.Globales;

@Entity
@Table(name = "cita")
public class Cita implements Comparable<Cita>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "paciente", nullable = false)
	private long paciente;
	
	@Column(name = "enfermero", nullable = true)
	private Long enfermero;
	
	@Column(name = "\"prueba complementaria\"", nullable = false)
	private long prueba;
	
	@Column(name = "estado", nullable = false)
	private long estado;
	
	@Column(name = "centro", nullable = false)
	private long centro;
	
	@Column(name = "fecha", nullable = false)
	@OrderBy("fecha DESC")
	private Timestamp fecha;
	
	@Column(name = "observaciones", nullable = true)
	private String observaciones;
	
	@Column(name = "codigoqr", nullable = true)
	private String codigoqr;

	public Cita() {}
	
	public Cita(Object[] objeto) {
		this.setId(((BigInteger) objeto[0]).intValue());
		this.setPaciente(((BigInteger) objeto[1]).intValue());
		this.setPrueba(((BigInteger) objeto[2]).intValue());
		this.setEstado(((BigInteger) objeto[3]).intValue());
		this.setFecha((Timestamp) objeto[4]);
		this.setCentro(((BigInteger) objeto[5]).intValue());
		this.setObservaciones((String) objeto[6]);
		this.setCodigoqr((String) objeto[7]);
		if(objeto[8] != null) {			
			this.setEnfermero(Long.parseLong((String) objeto[8]));
		} else {
			this.setEnfermero(null);
		}
	}
	
	public Cita(long id, long paciente, long prueba, long estado, long centro, Timestamp fecha, String observaciones,
			String codigoqr) {
		this.id = id;
		this.paciente = paciente;
		this.enfermero = null;
		this.prueba = prueba;
		this.estado = estado;
		this.centro = centro;
		this.fecha = fecha;
		this.observaciones = observaciones;
		this.codigoqr = codigoqr;
	}
	
	public Cita(long id, long paciente, Long enfermero, long prueba, long estado, long centro, Timestamp fecha,
			String observaciones, String codigoqr) {
		this.id = id;
		this.paciente = paciente;
		this.enfermero = enfermero;
		this.prueba = prueba;
		this.estado = estado;
		this.centro = centro;
		this.fecha = fecha;
		this.observaciones = observaciones;
		this.codigoqr = codigoqr;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPaciente() {
		return paciente;
	}

	public void setPaciente(long paciente) {
		this.paciente = paciente;
	}
	
	public Long getEnfermero() {
		return enfermero;
	}

	public void setEnfermero(Long enfermero) {
		this.enfermero = enfermero;
	}

	public long getPrueba() {
		return prueba;
	}

	public void setPrueba(long prueba) {
		this.prueba = prueba;
	}

	public long getEstado() {
		return estado;
	}

	public void setEstado(long estado) {
		this.estado = estado;
	}
	
	public long getCentro() {
		return centro;
	}
	
	public void setCentro(long centro) {
		this.centro = centro;
	}
	
	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}


	public String getObservaciones() {
		return observaciones;
	}
	

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getCodigoqr() {
		return codigoqr;
	}

	public void setCodigoqr(String codigoqr) {
		this.codigoqr = codigoqr;
	}

	@Override
	public int compareTo(Cita citaAComparar) {
		return this.getFecha().before(citaAComparar.getFecha()) ? -1 : 1;
	}
}
