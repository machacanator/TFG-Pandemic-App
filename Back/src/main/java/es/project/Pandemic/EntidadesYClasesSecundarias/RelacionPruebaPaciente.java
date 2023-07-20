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
@Table(name = "pruebaspacientes")
public class RelacionPruebaPaciente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private long paciente;

	@Column(nullable = false)
	private long prueba;
	
	@Column(nullable = false)
	private boolean completada;

	public RelacionPruebaPaciente() {}
	
	public RelacionPruebaPaciente(Object[] objeto) {
		this.setId(((BigInteger) objeto[0]).intValue());
		this.setPaciente(((BigInteger) objeto[1]).intValue());
		this.setPrueba(((BigInteger) objeto[2]).intValue());
		this.setCompletada((boolean) objeto[3]);
	}

	public RelacionPruebaPaciente(long id, long paciente, long prueba, boolean completada) {
		this.id = id;
		this.paciente = paciente;
		this.prueba = prueba;
		this.completada = completada;
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

	public long getPrueba() {
		return prueba;
	}

	public void setPrueba(long prueba) {
		this.prueba = prueba;
	}

	public boolean isCompletada() {
		return completada;
	}

	public void setCompletada(boolean completada) {
		this.completada = completada;
	}
	
}
