package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "horariospruebascentros")
public class HorarioPruebaCentro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "centro",nullable = false)
	private int idCentro;
	
	@Column(name = "prueba", nullable = false)
	private int idPrueba;
	
	@Column(name = "horainicio", nullable = false)
	private Time horaInicio;
	
	@Column(name = "horafin", nullable = false)
	private Time horaFin;
	
	public HorarioPruebaCentro() {}
	
	public HorarioPruebaCentro(Object[] objeto) {
		this.setId(((BigInteger) objeto[0]).intValue());
		this.setIdCentro(((BigInteger) objeto[1]).intValue());
		this.setIdPrueba(((BigInteger) objeto[2]).intValue());
		this.setHoraInicio((Time)objeto[3]);
		this.setHoraFin((Time)objeto[4]);
	}

	public HorarioPruebaCentro(int id, int idCentro, int idPrueba, Time horaInicio, Time horaFin) {
		this.id = id;
		this.idPrueba = idPrueba;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdPrueba() {
		return idPrueba;
	}

	public void setIdPrueba(int idPrueba) {
		this.idPrueba = idPrueba;
	}
	
	public int getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(int idCentro) {
		this.idCentro = idCentro;
	}

	public Time getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Time getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}
	
}
