package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "requisitospruebas")
public class RequisitosPrueba {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private long prueba;
	
	@Column(name="muyrecomendable", nullable = false)
	private boolean muyRecomendable;
	
	@Column(name = "edadinicio")
	private Long edadInicio;
	
	@Column(name = "edadfin")
	private Long edadFin;
	
	public RequisitosPrueba() {}
	
	public RequisitosPrueba(Object[] objeto) {
		this.setId(((BigInteger) objeto[0]).longValue());
		this.setPrueba(((BigInteger) objeto[1]).longValue());
		this.setMuyRecomendable((Boolean) objeto[2]);
		this.setEdadInicio(objeto[3] != null ? ((BigInteger) objeto[3]).longValue() : null);
		this.setEdadFin(objeto[4] != null ? ((BigInteger) objeto[4]).longValue() : null);
	}

	public RequisitosPrueba(long id, long prueba, boolean muyRecomendable, Long edadInicio, Long edadFin) {
		this.id = id;
		this.prueba = prueba;
		this.muyRecomendable = muyRecomendable;
		this.edadInicio = edadInicio;
		this.edadFin = edadFin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPrueba() {
		return prueba;
	}

	public void setPrueba(long prueba) {
		this.prueba = prueba;
	}

	public boolean isMuyRecomendable() {
		return muyRecomendable;
	}

	public void setMuyRecomendable(boolean muyRecomendable) {
		this.muyRecomendable = muyRecomendable;
	}

	public Long getEdadInicio() {
		return edadInicio;
	}

	public void setEdadInicio(Long edadInicio) {
		this.edadInicio = edadInicio;
	}

	public Long getEdadFin() {
		return edadFin;
	}

	public void setEdadFin(Long edadFin) {
		this.edadFin = edadFin;
	}
	
	public boolean cumpleRequisitoPaciente(int edadPaciente) {
		return (edadInicio == null && edadFin == null)
			|| (edadInicio == null && edadFin >= edadPaciente)
			|| (edadFin == null && edadInicio <= edadPaciente)
			|| (edadInicio <= edadPaciente && edadFin >= edadPaciente);
	}
	
}
