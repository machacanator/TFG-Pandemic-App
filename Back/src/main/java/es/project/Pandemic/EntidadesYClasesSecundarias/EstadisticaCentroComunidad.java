package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.List;

public class EstadisticaCentroComunidad {
	private long idCentro;
	private String nombreCentro;
	private List<Long> citas;

	public EstadisticaCentroComunidad(long idCentro, String nombreCentro, List<Long> citas) {
		this.idCentro = idCentro;
		this.nombreCentro = nombreCentro;
		this.citas = citas;
	}

	public long getIdCentro() {
		return idCentro;
	}

	public void setIdCentro(long idCentro) {
		this.idCentro = idCentro;
	}
	
	public String getNombreCentro() {
		return nombreCentro;
	}

	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}

	public List<Long> getCitas() {
		return citas;
	}

	public void setCitas(List<Long> citas) {
		this.citas = citas;
	}
}
