package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.DateTimeComparator;

public class Horario {
	
	private long id;
	private Time horaInicio;
	private Time horaFin;
	
	public Horario(long id, Time horaInicio, Time horaFin) {
		this.id = id;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}
		
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	public boolean estaDentroDelHorario(Time horaComprobar) {
		DateTimeComparator comparador = DateTimeComparator.getTimeOnlyInstance();
		boolean a = comparador.compare(horaComprobar, horaInicio) >= 0;
		boolean b = comparador.compare(horaComprobar, horaFin) <= 0;
		return (comparador.compare(horaComprobar, horaInicio) >= 0) && (comparador.compare(horaComprobar, horaFin) <= 0);
		/*boolean a = horaComprobar.after(horaInicio) || horaComprobar.equals(horaInicio);
		boolean b = horaComprobar.before(horaFin) || horaComprobar.equals(horaFin);
		return (horaComprobar.after(horaInicio) || horaComprobar.equals(horaInicio)) && (horaComprobar.before(horaFin) || horaComprobar.equals(horaFin));*/
		/*Calendar calendarioFechaAComprobar = GregorianCalendar.getInstance();
		Calendar calendarioHoraInicio = GregorianCalendar.getInstance();
		Calendar calendarioHoraFin = GregorianCalendar.getInstance();
		calendarioFechaAComprobar.setTime(fecha);
		calendarioHoraInicio.setTime(horaInicio);
		calendarioHoraFin.setTime(horaFin);
		int horaAComprobar = calendarioFechaAComprobar.get(Calendar.HOUR_OF_DAY);
		int minutosAComprobar = calendarioFechaAComprobar.get(Calendar.MINUTE);
		int horaInicioHorario = calendarioHoraInicio.get(Calendar.HOUR_OF_DAY);
		int minutosInicioHorario = calendarioHoraInicio.get(Calendar.MINUTE);
		int horaFinHorario = calendarioHoraFin.get(Calendar.HOUR_OF_DAY);
		int minutosFinHorario = calendarioHoraFin.get(Calendar.MINUTE);
		
		if(horaAComprobar == horaInicioHorario) {
			return minutosAComprobar >= minutosInicioHorario;
		} if(horaAComprobar == horaFinHorario) {
			return minutosAComprobar <= minutosFinHorario;
		}
		return horaAComprobar > horaInicioHorario && horaAComprobar < horaFinHorario;*/
	}
	
	public List<String> getCitasDisponiblesHorario(long duracionCitaMinutos) {
		List<String> listadoCitas = new ArrayList<>();
		long horaInicioEnSegundos = convertirASegundos(horaInicio.toString());
		long horaFinEnSegundos = convertirASegundos(horaFin.toString());
		long duracionCitaSegundos = duracionCitaMinutos * 60;
		
		long numeroDeCitas = (horaFinEnSegundos-horaInicioEnSegundos)%duracionCitaSegundos > 0
		? ((horaFinEnSegundos-horaInicioEnSegundos)/duracionCitaSegundos)
		: ((horaFinEnSegundos-horaInicioEnSegundos)/duracionCitaSegundos) - 1; // Si hay una cita que comienza exactamente cuando termina el horario, esta se elimina
		
		long tiempoSegundosProximaCita = horaInicioEnSegundos;
		for(int i = 0; i <= numeroDeCitas ; i++) {
			listadoCitas.add(convertirACadena(tiempoSegundosProximaCita));
			tiempoSegundosProximaCita = tiempoSegundosProximaCita + duracionCitaSegundos;
		}
		return listadoCitas;
	}
	
	private long convertirASegundos(String tiempo) {
		String[] cadenaFragmentada = tiempo.split(":");
		return Integer.parseInt(cadenaFragmentada[0]) * 60 * 60 + Integer.parseInt(cadenaFragmentada[1]) * 60 + Integer.parseInt(cadenaFragmentada[2]);  
	}
	
	private String convertirACadena(long tiempoEnSegundos) {
		long horas = tiempoEnSegundos / ( 60 * 60 );
		long minutos = (tiempoEnSegundos % ( 60 * 60 ) ) / 60;
		long segundos = tiempoEnSegundos % 60;
		
		return (horas < 10 ? "0"+horas :  horas)+":"+(minutos < 10 ? "0"+minutos :  minutos)+":"+(segundos < 10 ? "0"+segundos :  segundos);
	}
	
}
