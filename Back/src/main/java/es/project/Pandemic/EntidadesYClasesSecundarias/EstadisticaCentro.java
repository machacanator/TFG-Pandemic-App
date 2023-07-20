package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.ArrayList;
import java.util.List;

public class EstadisticaCentro {
	
	class CitasPrueba {
		private Long idPrueba;
		private Long total;
		
		public CitasPrueba(Long idPrueba, Long total) {
			this.idPrueba = idPrueba;
			this.total = total;
		}
		
		public Long getIdPrueba() {
			return idPrueba;
		}
		public void setIdPrueba(Long idPrueba) {
			this.idPrueba = idPrueba;
		}
		public Long getTotal() {
			return total;
		}
		public void setTotal(Long total) {
			this.total = total;
		}
	}
	
	private String nombreCentro;
	private List<Long> totalCitas;
	private List<CitasPrueba> pruebas;

	public EstadisticaCentro(String nombreCentro, List<Long> totalCitas, List<CitasPrueba> pruebas) {
		this.nombreCentro = nombreCentro;
		this.totalCitas = totalCitas;
		this.pruebas = pruebas;
	}
	
	public EstadisticaCentro(String nombreCentro, List<Long> totalCitas, List<Long> idsPruebas, List<Long> totalesPorPrueba) {
		this.nombreCentro = nombreCentro;
		this.totalCitas = totalCitas;
		this.setPruebas(idsPruebas, totalesPorPrueba);
	}

	public String getNombreCentro() {
		return nombreCentro;
	}

	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}

	public List<Long> getTotalCitas() {
		return totalCitas;
	}

	public void setTotalCitas(List<Long> totalCitas) {
		this.totalCitas = totalCitas;
	}

	public List<CitasPrueba> getPruebas() {
		return pruebas;
	}

	public void setPruebas(List<CitasPrueba> pruebas) {
		this.pruebas = pruebas;
	}
	
	public void setPruebas(List<Long> idsPruebas, List<Long> totalesPorPrueba) {
		if(idsPruebas.size() == totalesPorPrueba.size()) {
			List<CitasPrueba> listaCitasPrueba = new ArrayList<CitasPrueba>();
			for(int indice = 0; indice < idsPruebas.size(); indice++) {
				listaCitasPrueba.add(new CitasPrueba(idsPruebas.get(indice), totalesPorPrueba.get(indice)));
			}
			this.setPruebas(listaCitasPrueba);
		} else {			
			throw new ArithmeticException();
		}
	}
}
