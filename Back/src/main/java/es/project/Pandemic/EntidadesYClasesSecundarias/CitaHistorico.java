package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.math.BigInteger;
import java.sql.Timestamp;

public class CitaHistorico extends Cita{
	private String nombreCompletoPaciente;

	public CitaHistorico(Object[] objeto) {
		if(objeto.length == 11) {			
			this.setId(((BigInteger) objeto[0]).intValue());
			this.setPaciente(((BigInteger) objeto[1]).intValue());
			this.setNombreCompletoPaciente((String) objeto[2], (String) objeto[3]);
			this.setPrueba(((BigInteger) objeto[4]).intValue());
			this.setEstado(((BigInteger) objeto[5]).intValue());
			this.setFecha((Timestamp) objeto[6]);
			this.setCentro(((BigInteger) objeto[7]).intValue());
			this.setObservaciones((String) objeto[8]);
			this.setCodigoqr((String) objeto[9]);
		} else {
			this.setId(((BigInteger) objeto[0]).intValue());
			this.setPaciente(((BigInteger) objeto[1]).intValue());
			this.setNombreCompletoPaciente(null);
			this.setPrueba(((BigInteger) objeto[2]).intValue());
			this.setEstado(((BigInteger) objeto[3]).intValue());
			this.setFecha((Timestamp) objeto[4]);
			this.setCentro(((BigInteger) objeto[5]).intValue());
			this.setObservaciones((String) objeto[6]);
			this.setCodigoqr((String) objeto[7]);
		}
	}
	
	public CitaHistorico(long id, long idPaciente, String nombrePaciente, String apellidosPaciente, long prueba, long estado, long centro, Timestamp fecha, String observaciones,
			String codigoqr) {
		super(id, idPaciente, prueba, estado, centro, fecha, observaciones, codigoqr);
		this.nombreCompletoPaciente = apellidosPaciente + ", " + nombrePaciente;
	}

	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}

	public void setNombreCompletoPaciente(String nombrePaciente, String apellidosPaciente) {
		this.nombreCompletoPaciente = apellidosPaciente + ", " + nombrePaciente;
	}
	
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}
}
