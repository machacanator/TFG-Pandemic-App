package es.project.Pandemic.Constantes;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import es.project.Pandemic.Excepciones.MensajeError;


public class Errores {
	
	@FunctionalInterface
	public interface Function2<One, Two> {
	    public Two apply(One one);
	}
	
	@FunctionalInterface
	public interface Function3<One, Two, Three> {
	    public Three apply(One one, Two two);
	}
	
	public final class PARAMETROS {
		public static final String PARAMETRO_CODIGO = "codigo";
		public static final String PARAMETRO_MENSAJE = "mensaje";
		public static final MensajeError PARAMETROS_NO_VALIDOS = new MensajeError("parametros_no_validos.error", "Alguno de los parametros insertados no es valido");
		public static final Function2<String, MensajeError> PARAMETRO_NO_VALIDO = (String nombreParametro) -> new MensajeError(null, "El parametro '"+nombreParametro+"' proporcionado no es valido");
	}
	
	public final class LOGIN {
		public static final MensajeError ERROR_INICIO_SESION = new MensajeError("inicio_sesion.error", "Se ha producido un error al procesar la petición de iniciar sesión");
		public static final MensajeError USUARIO_NO_EXISTE = new MensajeError("inicio_sesion.usuario_no_existe", "El usuario con el que se ha intentado loguear no existe");
		public static final MensajeError ERROR_CREDENCIALES = new MensajeError("inicio_sesion.credenciales", "Credenciales incorrectas");
	};
	
	public final class AUTENTICACION {
		public static final MensajeError ERROR_AUTENTICACION = new MensajeError("autenticacion.error", "Usuario no autorizado");
		public static final MensajeError TOKEN_EXPIRADO = new MensajeError("token_expirado.error", "Token expirado, no se puede autenticar el usuario");
	};
	
	public final class RESPUESTA {
		public static final MensajeError ERROR_CUERPO_RESPUESTA = new MensajeError(null, "Error al tratar de escribir el cuerpo de la respuesta tras haberse producido una excepcion");
	};
	
	public final class SECCIONES {
		public static final Function3<Long, String, MensajeError> SECCION_SIN_PADRE = (Long id, String seccion) -> new MensajeError(null, "No se encuentra una seccion padre con id "+id+" para la seccion "+seccion+"");
	};
	
	public final class NOTIFICACIONES {
		public static final MensajeError NOTIFICACION_YA_VISUALIZADA = new MensajeError("notificacion_ya_visualizada.error", "La notificacion ya estaba maracada como visualizada");
		public static final MensajeError NOTIFICACION_NO_EXISTE = new MensajeError("notificacion_inexistente.error", "La notificacion que trata de actualizar o borrar no existe");
		public static final MensajeError ERROR_PERMISOS_NOTIFICACION = new MensajeError("no_permitido_notificacion.error", "El usuario no tiene permitido actualizar o borrar la notificacion proporcionada");
		public static final MensajeError ERROR_BORRAR_NOTIFICACION = new MensajeError("borrar_notificacion.error", "No se ha podido borrar la notificacion");
		public static final MensajeError ERROR_MARCAR_NOTIFICACION = new MensajeError("marcar_notificacion.error", "No se ha podido marcar como visualizada la notificacion");
	};
	
	public final class CITAS {
		public static final MensajeError CITA_NO_EXISTE = new MensajeError("cita_inexistente.error", "La cita que trata de actualizar o borrar no existe");
		public static final MensajeError ERROR_PERMISOS_CITA = new MensajeError("no_permitido_cita.error", "El usuario no tiene permitido actualizar o borrar la cita proporcionada");
		public static final MensajeError ERROR_PEDIR_CITA = new MensajeError("pedir_cita.error", "No se ha podido pedir la cita solicitada");
		public static final MensajeError ERROR_CAMBIAR_CITA = new MensajeError("cambiar_cita.error", "No se ha podido modificar la cita proporcionada");
		public static final MensajeError ERROR_BORRAR_CITA = new MensajeError("borrar_cita.error", "No se ha podido borrar la cita");
		public static final MensajeError ERROR_HORA_INVALIDA = new MensajeError("hora_invalida.error", "La hora de la cita que quiere crear o actualizar no es valida");
		public static final MensajeError ERROR_FECHA_INVALIDA = new MensajeError("fecha_invalida.error", "La fecha de la cita que quiere crear o actualizar no es valida");
		public static final MensajeError ERROR_FECHA_CITA_OCUPADA = new MensajeError("fecha_cita_ocupada.error", "La fecha de la cita que quiere crear o actualizar ya se encuentra ocupada por otra cita");
	};
	
	public final class DOCUMENTOS {
		public static final MensajeError ERROR_AVISO_LEGAL = new MensajeError("error_aviso_legal.error", "Error al obtener el archivo de aviso legal");
		public static final MensajeError ERROR_TERMINOS_CONDICIONES = new MensajeError("error_terminos_condiciones.error", "Error al obtener el archivo de términos y condiciones");
	};
	
	public final class IMAGENES {
		public static final MensajeError ERROR_PROCESAR_IMAGEN = new MensajeError("error_procesar_imagen.error", "Error al procesar la imagen");
	};
	
	public final class PAGINACION {
		public static final MensajeError PAGINA_NO_EXISTE = new MensajeError("pagina_no_existe.error", "La pagina solicitada no existe");
	};
	
	public final class PERSONAS {
		public static final MensajeError PERSONA_NO_EXISTE = new MensajeError("persona_no_existe.error", "No existe una persona con la id solicitada");
	};
	
	public final class ESTADISTICAS {
		public static final MensajeError ERROR_FECHA_FORMATO = new MensajeError("fecha_formato.error", "El formato de la fecha solicitada es erróneo");
		public static final MensajeError USUARIO_NO_EXISTE = new MensajeError("estadisticas_usuario.error", "El usuario del que quiere obtener las estadisticas no existe");
		public static final MensajeError CENTRO_NO_EXISTE = new MensajeError("estadisticas_centro.error", "El centro del que quiere obtener las estadisticas no existe");
	};
	
	public final class ADMINISTRACION {
		public static final MensajeError ADMINISTRADOR_SIN_CENTRO = new MensajeError("administrador_sin_centro.error", "No hay ningun centro asociado con el administrador proporcionado");
		public static final MensajeError NO_EXISTE_EMPLEADO = new MensajeError("no_existe_empleado.error", "No existe ningun empleado con la id solicitada");
		public static final MensajeError CANDIDATO_ES_EMPLEADO = new MensajeError("candidato_es_empleado.error", "El candidato que ha solicitado ya existe y es empleado");
		public static final MensajeError NO_EXISTE_CANDIDATO = new MensajeError("no_existe_candidato.error", "No existe ningun candidato con el número de seguridad social proporcionado");
		public static final MensajeError ERROR_NUMERO_SEGURIDAD_SOCIAL = new MensajeError("lista_pruebas_tratables.error", "El número de seguridad social proporcionado no es válido");
		public static final MensajeError ERROR_LISTA_PRUEBAS_TRATABLES = new MensajeError("lista_pruebas_tratables.error", "La lista de pruebas tratables proporcionada no es válida");
		public static final Function2<String, MensajeError> ERROR_IDENTIFICADOR_PRUEBA_TRATABLE = (String idPrueba) -> new MensajeError("identificador_prueba_tratable.error", "No se puede encontrar datos de la prueba con id: "+idPrueba+" de la lista de pruebas tratables proporcionada");
		public static final Function2<String, MensajeError> ERROR_IDENTIFICADOR_CENTRO = (String idCentro) -> new MensajeError("identificador_centro.error", "No se puede encontrar datos del centro con id: "+idCentro);
		public static final MensajeError NO_EXISTE_PRUEBA_CENTRO = new MensajeError("no_existe_prueba_centro.error", "No existe ninguna prueba en el centro con la id solicitada");
		public static final MensajeError ERROR_HORARIOS_PRUEBA_CENTRO = new MensajeError("horarios_prueba_centro.error", "Los nuevos horarios proporcionados tienen un formato incorrecto");
		public static final MensajeError NO_EXISTE_CARPETA_DE_PRUEBAS = new MensajeError("no_existe_carpeta_de_pruebas.error", "No existe ninguna carpeta de pruebas con la id solicitada");
		public static final MensajeError NO_EXISTE_PRUEBA = new MensajeError("no_existe_prueba.error", "No existe ninguna prueba con la id solicitada");
		public static final Function3<String, String, MensajeError> ERROR_PARAMETRO_REQUISITO = (String parametro, String indiceRequisito) -> new MensajeError("parametro_requisito.error", "Error al procesar el parametro: '"+parametro+"' del rango de edad con indice: "+indiceRequisito);
	};
	
}