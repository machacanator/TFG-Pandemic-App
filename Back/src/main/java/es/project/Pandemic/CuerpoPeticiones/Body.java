package es.project.Pandemic.CuerpoPeticiones;

public class Body{
	private String documentoIdentidad;
	private String contrasenna;
	public Body() {
	}
	public Body(String documentoIdentidad, String contrasenna) {
		this.documentoIdentidad = documentoIdentidad;
		this.contrasenna = contrasenna;
	}
	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}
	public void setDocumentoIdentidad(String documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}
	public String getContrasenna() {
		return contrasenna;
	}
	public void setContrasenna(String contrasenna) {
		this.contrasenna = contrasenna;
	}
}