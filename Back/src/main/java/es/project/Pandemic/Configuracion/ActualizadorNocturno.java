package es.project.Pandemic.Configuracion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import es.project.Pandemic.EntidadesYClasesSecundarias.CarpetaDePruebas;
import es.project.Pandemic.EntidadesYClasesSecundarias.Persona;
import es.project.Pandemic.EntidadesYClasesSecundarias.PruebaComplementaria;
import es.project.Pandemic.EntidadesYClasesSecundarias.RequisitosPrueba;
import es.project.Pandemic.Servicios.ServicioCitas;
import es.project.Pandemic.Servicios.ServicioPersonas;
import es.project.Pandemic.Servicios.ServicioPruebasComplementarias;

@Configuration
@EnableScheduling
public class ActualizadorNocturno {
	@Autowired
	private ServicioPersonas servicioPersonas;
	
	@Autowired
	private ServicioPruebasComplementarias servicioPruebasComplementarias;
	
	@Autowired
	private ServicioCitas servicioCitas;
	
	@Scheduled(cron = "0 0 0 * * MON,TUE,WED,THU,FRI")
	public void actualizarPruebasPacientes() {
		List<CarpetaDePruebas> listaCarpetas = servicioPruebasComplementarias.getTodasCarpetasPruebas();
		List<Persona> listaPersonas = servicioPersonas.getTodasPersonas();
		for(Persona persona : listaPersonas) {
			List<Long> idsPruebasAAnnadir = new ArrayList<>();
			for(CarpetaDePruebas carpeta : listaCarpetas) {
				PruebaComplementaria pruebaAAnnadir = procesarCarpetaDePruebasParaPersona(carpeta, persona);
				if(pruebaAAnnadir != null) idsPruebasAAnnadir.add(pruebaAAnnadir.getId());
			}
			for(Long idPrueba : idsPruebasAAnnadir) {
				servicioPruebasComplementarias.annadirNuevaPruebaAPaciente(idPrueba, persona.getId());
			}
		}
	}
	
	
	private PruebaComplementaria procesarCarpetaDePruebasParaPersona(CarpetaDePruebas carpeta, Persona persona) {
		List<PruebaComplementaria> listadoPruebasDeCarpeta = servicioPruebasComplementarias.getTodasLasPruebasDentroDeCarpetaDePruebas(carpeta.getId());
		for(int indicePrueba = 0; indicePrueba < listadoPruebasDeCarpeta.size(); indicePrueba++) {
			PruebaComplementaria prueba = listadoPruebasDeCarpeta.get(indicePrueba);
			if(servicioCitas.estaPruebaCompletadaParaPaciente(prueba.getId(), persona.getId())) {
				continue;
			} else if(
				(indicePrueba == 0 && cumplePersonaRequisitosDePrueba(prueba, persona))
				|| (indicePrueba > 0 && haPasadoTiempoSeparacionDeAnteriorPrueba(listadoPruebasDeCarpeta.get(indicePrueba - 1), persona) && cumplePersonaRequisitosDePrueba(prueba, persona))
			) {
				return prueba;
			} else {
				return null;
			}
		}
		return null;
	}
	
	private boolean cumplePersonaRequisitosDePrueba(PruebaComplementaria prueba, Persona persona) {
		List<RequisitosPrueba> requisitosDePrueba = servicioPruebasComplementarias.getRequisitosDePrueba(prueba.getId());
		int edadPersona = persona.getEdad();
		for(RequisitosPrueba requisito : requisitosDePrueba) {
			requisito.cumpleRequisitoPaciente(edadPersona);
		}
		return false;
	}
	
	private boolean haPasadoTiempoSeparacionDeAnteriorPrueba(PruebaComplementaria pruebaAnterior, Persona persona) {
		long diasSeparacionEntreHoyYCitaAnterior = Days.daysBetween(
				new DateTime(servicioCitas.getFechaUltimaCitaDePruebaParaPaciente(pruebaAnterior.getId(), persona.getId())),
				new DateTime(new Date())
		).getDays();
		return diasSeparacionEntreHoyYCitaAnterior >= pruebaAnterior.getSeparacionSiguientePrueba();
	}

}
