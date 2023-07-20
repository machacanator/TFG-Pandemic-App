package es.project.Pandemic.EntidadesYClasesSecundarias;

public class Pagina<T> {
	private T datos;
	private long numeroPagina;
	private long maxResultadosPagina;
	private long totalRegistros;
	
	public Pagina(T datos, long numeroPagina, long maxResultadosPagina, long totalRegistros) {
		this.datos = datos;
		this.numeroPagina = numeroPagina;
		this.maxResultadosPagina = maxResultadosPagina;
		this.totalRegistros = totalRegistros;
	}

	public T getDatos() {
		return datos;
	}

	public void setDatos(T datos) {
		this.datos = datos;
	}

	public long getNumeroPagina() {
		return numeroPagina;
	}

	public void setNumeroPagina(long numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	public long getMaxResultadosPagina() {
		return maxResultadosPagina;
	}

	public void setMaxResultadosPagina(long maxResultadosPagina) {
		this.maxResultadosPagina = maxResultadosPagina;
	}

	public long getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	
}
