export default class UrlAplicacion {
    public static BASE = 'http://localhost:8080/Pandemic/';
    public static INICIO_SESION = `${UrlAplicacion.BASE}iniciarSesion`;
    public static REFRESCAR_TOKEN = `${UrlAplicacion.BASE}refrescarToken`;
    public static ACCIONES = `${UrlAplicacion.BASE}acciones`;
    public static SECCIONES = `${UrlAplicacion.BASE}secciones`;
    public static SECCIONES__MENU = `${UrlAplicacion.BASE}secciones-menu`;
    public static USUARIO = `${UrlAplicacion.BASE}usuario`;
    public static PERSONA = `${UrlAplicacion.BASE}persona`;
    public static NOTIFICACIONES = `${UrlAplicacion.BASE}notificaciones`;
    public static CENTROS = `${UrlAplicacion.BASE}centros`;
    public static CITAS = `${UrlAplicacion.BASE}citas`;
    public static ESTADISTICAS = `${UrlAplicacion.BASE}estadisticas`;
    public static PRUEBAS_COMPLEMENTARIAS = `${UrlAplicacion.BASE}pruebas`;
    public static CARPETAS_PRUEBAS = `${UrlAplicacion.BASE}carpetas-pruebas`;
    public static DOCUMENTOS = `${UrlAplicacion.BASE}documentos`;
    public static ADMINISTRACION_PLANTILLA = `${UrlAplicacion.BASE}administracion-plantilla`;
    public static ADMINISTRACION_CENTRO = `${UrlAplicacion.BASE}administracion-centro`;
    public static ADMINISTRACION_PRUEBAS_CENTRO = `${UrlAplicacion.BASE}administracion-pruebas/pruebas-centro`;
    public static ADMINISTRACION_CENTROS_COMUNIDAD = `${UrlAplicacion.BASE}administracion-comunidad/centros-comunidad`;
    public static ADMINISTRACION_CARPETAS_DE_PRUEBAS_COMUNIDAD = `${UrlAplicacion.BASE}administracion-comunidad/carpetas-de-pruebas-comunidad`;
    public static ADMINISTRACION_PRUEBAS_COMUNIDAD = `${UrlAplicacion.BASE}administracion-comunidad/pruebas-comunidad`;
    
    public static HOME = `${UrlAplicacion.BASE}home`;
    public static TEST = `${UrlAplicacion.BASE}test`;
    public static DELAY = `${UrlAplicacion.BASE}delay`;
}