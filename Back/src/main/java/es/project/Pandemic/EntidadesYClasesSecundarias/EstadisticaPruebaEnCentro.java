package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.List;

public class EstadisticaPruebaEnCentro {
	private long idPrueba;
	private String nombrePrueba;
	private List<Long> citas;

	public EstadisticaPruebaEnCentro(long idPrueba, String nombrePrueba, List<Long> citas) {
		this.idPrueba = idPrueba;
		this.nombrePrueba = nombrePrueba;
		this.citas = citas;
	}

	public long getIdPrueba() {
		return idPrueba;
	}

	public void setIdPrueba(long idPrueba) {
		this.idPrueba = idPrueba;
	}
	
	public String getNombrePrueba() {
		return nombrePrueba;
	}

	public void setNombrePrueba(String nombrePrueba) {
		this.nombrePrueba = nombrePrueba;
	}

	public List<Long> getCitas() {
		return citas;
	}

	public void setCitas(List<Long> citas) {
		this.citas = citas;
	}
}
