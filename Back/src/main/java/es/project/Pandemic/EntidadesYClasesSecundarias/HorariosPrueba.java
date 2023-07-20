package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.List;

public class HorariosPrueba {
	
	private long idPrueba;
	private List<Horario> horarios;

	public HorariosPrueba(long idPrueba, List<Horario> horarios) {
		this.idPrueba = idPrueba;
		this.horarios = horarios;
	}

	public long getIdPrueba() {
		return idPrueba;
	}

	public void setIdPrueba(long idPrueba) {
		this.idPrueba = idPrueba;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}
	
}
