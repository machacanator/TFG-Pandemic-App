package es.project.Pandemic.Constantes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import es.project.Pandemic.Constantes.Errores.Function3;
import es.project.Pandemic.EntidadesYClasesSecundarias.CitaHistorico;
import es.project.Pandemic.EntidadesYClasesSecundarias.Pagina;
import es.project.Pandemic.Excepciones.MensajeError;

public class Globales {
	@FunctionalInterface
	public interface Function4<One, Two, Three, Four> {
	    public Three apply(One one, Two two, Three three);
	}
	
	public static final List<String> URL_PUBLICAS = Arrays.asList(
			"/swagger-ui/",
			"/webjars/",
			"/v3/",
			"/swagger-resources",
			"/Pandemic/iniciarSesion",
			"/Pandemic/refrescarToken",
			"/Pandemic/acciones",
			"/Pandemic/secciones",
			"/Pandemic/secciones-menu",
			"/Pandemic/documentos",
			"/Pandemic/centros",
			"/Pandemic/citas",
			"/Pandemic/notificaciones",
			"/Pandemic/pruebas",
			"/Pandemic/carpetas-pruebas",
			"/Pandemic/usuario",
			
			
			"/Pandemic/home",
			"/Pandemic/delay",
			"/Pandemic/test"
	);
	
	public final class TOKEN {		
		public static final String PARAMETRO_HEADER_TOKEN_ACCESO = "tokenAcceso";
		public static final String REFRESCO_TOKEN_CORRECTO = "Token refrescado correctamente";
		//public static final String PARAMETRO_HEADER_TOKEN_REFRESCO = "tokenRefresco";
	};
	
	public final class LOGIN {
		public static final String LOGIN_CORRECTO = "Login realizado correctamente";
		public static final String PARAMETRO_NOMBRE_USUARIO = "documentoIdentidad";
		public static final String PARAMETRO_CONTRASENNA_USUARIO = "contrasenna";
	};
	
	public final class ROLES {
		public static final long ENFERMERO = 1;
		public static final long ADMINISTRADOR_HOSPITAL = 2;
		public static final long ADMINISTRADOR_COMUNIDAD = 3;
	};
	
	public final class ESTADOS_CITAS {
		public static final long CANCELADA_INTERNO = -3;
		public static final long CANCELADA_USUARIO = -2;
		public static final long EXPIRADA = -1;
		public static final long PENDIENTE = 0;
		public static final long COMPLETADA = 1;
	};
	
	public final class NOTIFICACIONES {
		public static final String NOTIFICACION_BORRADA = "Notificacion borrada correctamente";
		public static final String NOTIFICACION_VISUALIZADA = "Notificacion marcada como visualizada por el usuario correctamente";
	};
	
	public final class CITAS {
		public static final String CITA_SOLICITADA = "Cita solicitada correctamente";
		public static final String CITA_CAMBIADA = "Cita modificada correctamente";
		public static final String CITA_GESTIONADA = "Cita gestionada correctamente";
		public static final String CITA_ELIMINADA = "Cita eliminada correctamente";
	};
	
	public final class EMPLEADOS {
		public static final String EMPLEADO_ELIMINADO = "Empleado eliminado de la plantilla correctamente";
		public static final String EMPLEADO_ACTUALIZADO = "Empleado actualizado correctamente";
		public static final String NUEVO_EMPLEADO_ANNADIDO = "Se ha añadido al nuevo empleado a la plantilla correctamente";
	};
	
	public final class PRUEBAS {
		public static final String PRUEBA_ANNADIDA = "Se ha añadido la prueba correctamente";
		public static final String PRUEBA_ACTUALIZADA = "Prueba actualizada correctamente";
		public static final String PRUEBA_ELIMINADA = "Prueba eliminada correctamente";
		public static final String CARPETA_PRUEBAS_ANNADIDA = "Se ha añadido la carpeta correctamente";
		public static final String CARPETA_PRUEBAS_ACTUALIZADA = "Carpeta actualizada correctamente";
		public static final String CARPETA_PRUEBAS_ELIMINADA = "Carpeta eliminada correctamente";
		public static final String PRUEBA_CENTRO_ANNADIDA = "Se ha añadido la prueba al centro correctamente";
		public static final String PRUEBA_CENTRO_ACTUALIZADA = "Se ha actualizado la prueba del centro correctamente";
		public static final String PRUEBA_CENTRO_ELIMINADA = "Prueba eliminada del centro correctamente";
		public static final String HORARIOS_PRUEBA_CENTRO_ACTUALIZADOS = "Horarios de prueba en centro actualizados correctamente";
	};
	
	public final class CENTROS {
		public static final String NUEVO_CENTRO_ANNADIDO = "Se ha añadido el centro correctamente";
		public static final String CENTRO_ACTUALIZADO = "Centro actualizado correctamente";
		public static final String CENTRO_ELIMINADO = "Centro eliminado correctamente";
	};
	
	public final class FECHAS {
		public static final DateFormat FORMATO_FECHA = new SimpleDateFormat("dd-MM-yyyy");
		public static final DateFormat FORMATO_HORA = new SimpleDateFormat("HH:mm:ss");
		public static final DateFormat FORMATO_FECHA_HORA = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	}
	
	public final class MODOS_HISTORICO {
		public static final String USUARIO = "usuario";
		public static final String ENFERMERO = "enfermero";
		public static final String ADMINISTRADOR = "administrador";
	}
}
