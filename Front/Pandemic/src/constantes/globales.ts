import { AbstractControl, FormGroup, ValidationErrors, ValidatorFn } from "@angular/forms";

export default class Globales {
    public static PETICION_REINTENTAR = 0;

    public static RESPUESTA = {
        PARAMETRO_TOKEN: 'tokenAcceso',
        PARAMETRO_ERROR_CODIGO: 'codigo',
        PARAMETRO_ERROR_MENSAJE: 'mensaje',
    }

    public static SECCIONES_ID = {
        CITAS: 1,
        NUEVA_CITA: 2,
        CITAS_PENDIENTES: 3,
        HISTORICO_CITAS_USUARIO: 4,
        PRUEBAS_COMPLEMENTARIAS: 5,
        INFORMACION_PRUEBAS: 6,
        MI_HOSPITAL: 7,
        INFORMACION_HOSPITAL: 8,
        AGENDA_CITAS: 9,
        DIARIO_CITAS: 10,
        GESTIONAR_CITA: 11,
        HISTORICO_CITAS_ENFERMERO: 12,
        CENTRO: 13,
        ADMINISTRAR_PLANTILLA: 14,
        ADMINISTRAR_PRUEBAS_EN_CENTRO: 15,
        ANNADIR_NUEVO_EMPLEADO: 16,
        ACTUALIZAR_PRUEBAS_EMPLEADO: 17,
        ANNADIR_NUEVA_PRUEBA_CENTRO: 18,
        ACTUALIZAR_HORARIOS_PRUEBA_CENTRO: 19,
        COMUNIDAD_AUTONOMA: 20,
        LISTADO_CARPETAS_PRUEBAS_COMUNIDAD: 21,
        LISTADO_PRUEBAS_COMUNIDAD: 22,
        LISTADO_CENTROS_COMUNIDAD: 23,
        ANNADIR_NUEVA_PRUEBA_COMUNIDAD: 24,
        ACTUALIZAR_PRUEBA_COMUNIDAD: 25,
        ANNADIR_NUEVO_CENTRO_COMUNIDAD: 26,
        ACTUALIZAR_CENTRO_COMUNIDAD: 27,
        ESTADISTICAS: 28,
        ESTADISTICAS_PERSONALES: 29,
        ESTADISTICAS_EMPLEADOS: 30,
        ESTADISTICAS_CENTRO: 31,
        ESTADISTICAS_CENTROS_COMUNIDAD: 32,
        ESTADISTICAS_COMUNIDAD: 33,
    }
    
    public static MENU_OPCIONES = {
        [this.SECCIONES_ID.CITAS]: {titulo: 'Citas', url: ''},
        [this.SECCIONES_ID.NUEVA_CITA]: {titulo: 'Nueva Cita', url: 'nueva-cita'},
        [this.SECCIONES_ID.CITAS_PENDIENTES]: {titulo: 'Citas pendientes', url: 'citas-pendientes'},
        [this.SECCIONES_ID.HISTORICO_CITAS_USUARIO]: {titulo: 'Histórico', url: 'historico-citas-usuario'},
        [this.SECCIONES_ID.PRUEBAS_COMPLEMENTARIAS]: {titulo: 'Pruebas Complementarias', url: ''},
        [this.SECCIONES_ID.INFORMACION_PRUEBAS]: {titulo: 'Información', url: 'informacion-pruebas'},
        [this.SECCIONES_ID.MI_HOSPITAL]: {titulo: 'Mi centro', url: ''},
        [this.SECCIONES_ID.INFORMACION_HOSPITAL]: {titulo: 'Información', url: 'mi-centro'},
        [this.SECCIONES_ID.AGENDA_CITAS]: {titulo: 'Agenda de citas', url: ''},
        [this.SECCIONES_ID.DIARIO_CITAS]: {titulo: 'Diario de citas', url: 'diario-citas'},
        [this.SECCIONES_ID.GESTIONAR_CITA]: {titulo: 'Gestionar cita', url: 'gestionar-cita'},
        [this.SECCIONES_ID.HISTORICO_CITAS_ENFERMERO]: {titulo: 'Histórico de citas', url: 'historico-citas-enfermero'},
        [this.SECCIONES_ID.CENTRO]: {titulo: 'Administración Centro', url: ''},
        [this.SECCIONES_ID.ESTADISTICAS]: {titulo: 'Estadísticas', url: ''},
        [this.SECCIONES_ID.ESTADISTICAS_PERSONALES]: {titulo: 'Personales', url: 'estadisticas-enfermero'},
        [this.SECCIONES_ID.ESTADISTICAS_CENTRO]: {titulo: 'Centro', url: 'estadisticas-centro'},
        [this.SECCIONES_ID.ADMINISTRAR_PLANTILLA]: {titulo: 'Plantilla', url: 'administrar-plantilla-centro'},
        [this.SECCIONES_ID.ADMINISTRAR_PRUEBAS_EN_CENTRO]: {titulo: 'Pruebas Complementarias', url: 'administrar-pruebas-centro'},
        [this.SECCIONES_ID.ANNADIR_NUEVO_EMPLEADO]: {titulo: 'Añadir nuevo empleado', url: 'annadir-nuevo-empleado'},
        [this.SECCIONES_ID.ACTUALIZAR_PRUEBAS_EMPLEADO]: {titulo: 'Actualizar pruebas de empleado', url: 'actualizar-pruebas-empleado'},
        [this.SECCIONES_ID.ANNADIR_NUEVA_PRUEBA_CENTRO]: {titulo: 'Añadir nueva prueba a centro', url: 'annadir-nueva-prueba-centro'},
        [this.SECCIONES_ID.ACTUALIZAR_HORARIOS_PRUEBA_CENTRO]: {titulo: 'Actualizar horarios de prueba en centro', url: 'actualizar-horarios-prueba-centro'},
        [this.SECCIONES_ID.ESTADISTICAS_EMPLEADOS]: {titulo: 'Empleados', url: 'estadisticas-empleados-centro'},
        [this.SECCIONES_ID.COMUNIDAD_AUTONOMA]: {titulo: 'Administración Comunidad Autónoma', url: ''},
        [this.SECCIONES_ID.LISTADO_CARPETAS_PRUEBAS_COMUNIDAD]: {titulo: 'Pruebas', url: 'administrar-pruebas-comunidad/carpetas-pruebas'},
        [this.SECCIONES_ID.LISTADO_PRUEBAS_COMUNIDAD]: {titulo: 'Pruebas', url: 'administrar-pruebas-comunidad/pruebas'},
        [this.SECCIONES_ID.LISTADO_CENTROS_COMUNIDAD]: {titulo: 'Centros', url: 'administrar-centros-comunidad'},
        [this.SECCIONES_ID.ESTADISTICAS_CENTROS_COMUNIDAD]: {titulo: 'Centros', url: 'estadisticas-centros-comunidad'},
        [this.SECCIONES_ID.ESTADISTICAS_COMUNIDAD]: {titulo: 'Comunidad', url: 'estadisticas-comunidad'},
        [this.SECCIONES_ID.ANNADIR_NUEVA_PRUEBA_COMUNIDAD]: {titulo: 'Añadir nueva prueba', url: 'administrar-pruebas-comunidad/nueva-prueba'},
        [this.SECCIONES_ID.ACTUALIZAR_PRUEBA_COMUNIDAD]: {titulo: 'Añadir nueva prueba', url: 'administrar-pruebas-comunidad/actualizar-prueba'},
        [this.SECCIONES_ID.ANNADIR_NUEVO_CENTRO_COMUNIDAD]: {titulo: 'Añadir nuevo centro', url: 'annadir-centro-comunidad'},
        [this.SECCIONES_ID.ACTUALIZAR_CENTRO_COMUNIDAD]: {titulo: 'Actualizar centro', url: 'actualizar-centro-comunidad'},
    }

    public static TIPOS_POPUP = {
        CORRECTO: 1,
        ERROR: 2,
        AVISO: 3,
    }

    // Milisegundos a distintas cantidades de tiempo
    public static SEGUNDOS = 1000;
    public static MINUTO = 60 * this.SEGUNDOS;
    public static HORA = 60 * this.MINUTO;
    public static DIA = 24 * this.HORA;
    public static SEMANA = 7 * this.DIA;
    public static MES = 30.5 * this.DIA;
    public static ANNO = 365.5 * this.DIA;

    
    public static FORMATO_FECHA_MES = "MM-YYYY";
    public static FORMATO_FECHA_NACIONAL = "DD-MM-YYYY";
    public static FORMATO_FECHA_INTERNACIONAL = "MM-DD-YYYY";
    public static FORMATO_HORA = "HH:mm:ss";

    public static MAXIMO_IMAGENES_CENTRO = 4;
    public static MAXIMO_TAMAÑO_IMAGEN = 1048576 * 10; // 10 MB
    
    public static TEXTO_PAGINADOR = {
        SIGUIENTE_PAGINA: 'Siguiente página',
        PAGINA_ANTERIOR: 'Página anterior',
        PRIMERA_PAGINA: 'Ir a la primera página',
        ULTIMA_PAGINA: 'Ir a la última página',
        RESULTADOS_POR_PAGINA: 'Resultados por página',
    }

    public static CODIGOS_NOTIFICACIONES = {
        'nueva_cita_creada': {
            mensaje: 'Se le asignado una nueva cita a las ###',
            boton: {texto: 'Ver citas pendientes', redireccion: 'citas-pendientes'},
        },
        'cita_cambiada': {
            mensaje: 'Se ha cambiado su cita a las ###',
            boton: {texto: 'Ver citas pendientes', redireccion: 'citas-pendientes'},
        },
        'nueva_prueba_complementaria_muy_recomendable': {
            mensaje: 'Hay una nueva prueba muy recomendada disponible (###)',
            boton: {texto: 'Pedir cita', redireccion: 'nueva-cita'},
        },
        'nueva_prueba_complementaria_opcional': {
            mensaje: 'Hay una nueva prueba disponible (###)',
            boton: {texto: 'Pedir cita', redireccion: 'nueva-cita'},
        },
        'sin_boton': {
            mensaje: 'Notificacion sin boton',
            boton: {},
        },
    }

    public static MENSAJES = {
        CITA_SOLICITADA: 'Su cita ha sido solicitada correctamente, en breves momentos recibirá una notificación',
        CITA_CAMBIADA: 'Su cita ha sido cambiada correctamente, en breves momentos recibirá una notificación',
        EMPLEADO_ANNADIDO: 'Se ha añadido un nuevo empleado correctamente',
        EMPLEADO_ACTUALIZADO: 'Se ha actualizado el empleado correctamente',
        EMPLEADO_ELIMINADO: 'El empleado ha sido dado de baja correctamente',
        CENTRO_ANNADIDO: 'Se ha añadido un nuevo centro correctamente',
        CENTRO_ELIMINADO: 'Se ha eliminado el centro correctamente',
        PRUEBA_CENTRO_ANNADIDA: 'Se ha añadido un nueva prueba al centro correctamente',
        HORARIOS_PRUEBA_CENTRO_ACTUALIZADOS: 'Se han actualizado los horarios de la prueba correctamente', 
        PRUEBA_CENTRO_ELIMINADA: 'Se ha eliminado la prueba del centro correctamente',
        CARPETA_PRUEBAS_ANNADIDA: 'Se ha añadido la nueva carpeta de pruebas correctamente',
        CARPETA_PRUEBAS_ACTUALIZADA: 'Se ha actualizado la carpeta de pruebas correctamente',
        CARPETA_PRUEBAS_ELIMINADA: 'Se ha eliminado la carpeta de pruebas correctamente',
        PRUEBA_ANNADIDA: 'Se ha añadido una nueva prueba correctamente',
        PRUEBA_ACTUALIZADA: 'Se ha actualizado la prueba correctamente',
        PRUEBA_ELIMINADA: 'Se ha eliminado la prueba correctamente',
    }

    public static NOMBRES_FICHEROS = {
        AVISO_LEGAL: 'PandemicAvisoLegal',
        TERMINOS_Y_CONDICIONES: 'PandemicTérminosYCondiciones',
    }

    public static TIPOS_GRAFICOS = {
        GRAFICO_BARRAS_VERTICALES: 1,
        GRAFICO_CIRCULAR: 2,
        GRAFICO_LINEAS: 3,
    }

    public static TIPOS_RANGOS_EDAD = {
        MENOR_QUE: 1,
        ENTRE: 2,
        MAYOR_QUE: 3,
        TODOS: 4,
    }

    public static TIPOS_SEPARACION_ENTRE_PRUEBAS = {
        DIAS: 1,
        SEMANAS: 2,
        MESES: 3,
        ANNOS: 4,
    }

    public static LISTADO_MESES = [
        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ];

    public static LISTADO_MUNICIPIOS = [
        'Acebeda (La)',
        'Ajalvir',
        'Alameda del Valle',
        'Alamo (El)',
        'Alcalá de Henares',
        'Alcobendas',
        'Alcorcón',
        'Aldea del Fresno',
        'Algete',
        'Alpedrete',
        'Ambite',
        'Anchuelo',
        'Aranjuez',
        'Arganda del Rey',
        'Arroyomolinos',
        'Atazar (El)',
        'Batres',
        'Becerril de la Sierra',
        'Belmonte de Tajo',
        'Berzosa del Lozoya',
        'Berrueco (El)',
        'Boadilla del Monte',
        'Boalo (El)',
        'Braojos',
        'Brea de Tajo',
        'Brunete',
        'Buitrago del Lozoya',
        'Bustarviejo',
        'Cabanillas de la Sierra',
        'Cabrera (La)',
        'Cadalso de los Vidrios',
        'Camarma de Esteruelas',
        'Campo Real',
        'Canencia',
        'Carabaña',
        'Casarrubuelos',
        'Cenicientos',
        'Cercedilla',
        'Cervera de Buitrago',
        'Ciempozuelos',
        'Cobeña',
        'Colmenar del Arroyo',
        'Colmenar de Oreja',
        'Colmenarejo',
        'Colmenar Viejo',
        'Collado Mediano',
        'Collado Villalba',
        'Corpa',
        'Coslada',
        'Cubas de la Sagra',
        'Chapinería',
        'Chinchón',
        'Daganzo de Arriba',
        'Escorial (El)',
        'Estremera',
        'Fresnedillas de la Oliva',
        'Fresno de Torote',
        'Fuenlabrada',
        'Fuente el Saz de Jarama',
        'Fuentidueña de Tajo',
        'Galapagar',
        'Garganta de los Montes',
        'Gargantilla del Lozoya y Pinilla de Buitrago',
        'Gascones',
        'Getafe',
        'Griñón',
        'Guadalix de la Sierra',
        'Guadarrama',
        'Hiruela (La)',
        'Horcajo de la Sierra-Aoslos',
        'Horcajuelo de la Sierra',
        'Hoyo de Manzanares',
        'Humanes de Madrid',
        'Leganés',
        'Loeches',
        'Lozoya',
        'Madarcos',
        'Madrid',
        'Majadahonda',
        'Manzanares el Real',
        'Meco',
        'Mejorada del Campo',
        'Miraflores de la Sierra',
        'Molar (El)',
        'Molinos (Los)',
        'Montejo de la Sierra',
        'Moraleja de Enmedio',
        'Moralzarzal',
        'Morata de Tajuña',
        'Móstoles',
        'Navacerrada',
        'Navalafuente',
        'Navalagamella',
        'Navalcarnero',
        'Navarredonda y San Mamés',
        'Navas del Rey',
        'Nuevo Baztán',
        'Olmeda de las Fuentes',
        'Orusco de Tajuña',
        'Paracuellos de Jarama',
        'Parla',
        'Patones',
        'Pedrezuela',
        'Pelayos de la Presa',
        'Perales de Tajuña',
        'Pezuela de las Torres',
        'Pinilla del Valle',
        'Pinto',
        'Piñuecar-Gandullas',
        'Pozuelo de Alarcón',
        'Pozuelo del Rey',
        'Prádena del Rincón',
        'Puebla de la Sierra',
        'Quijorna',
        'Rascafría',
        'Redueña',
        'Ribatejada',
        'Rivas-Vaciamadrid',
        'Robledillo de la Jara',
        'Robledo de Chavela',
        'Robregordo',
        'Rozas de Madrid (Las)',
        'Rozas de Puerto Real',
        'San Agustín del Guadalix',
        'San Fernando de Henares',
        'San Lorenzo de El Escorial',
        'San Martín de la Vega',
        'San Martín de Valdeiglesias',
        'San Sebastián de los Reyes',
        'Santa María de la Alameda',
        'Santorcaz',
        'Santos de la Humosa (Los)',
        'Serna del Monte (La)',
        'Serranillos del Valle',
        'Sevilla la Nueva',
        'Somosierra',
        'Soto del Real',
        'Talamanca de Jarama',
        'Tielmes',
        'Titulcia',
        'Torrejón de Ardoz',
        'Torrejón de la Calzada',
        'Torrejón de Velasco',
        'Torrelaguna',
        'Torrelodones',
        'Torremocha de Jarama',
        'Torres de la Alameda',
        'Valdaracete',
        'Valdeavero',
        'Valdelaguna',
        'Valdemanco',
        'Valdemaqueda',
        'Valdemorillo',
        'Valdemoro',
        'Valdeolmos-Alalpardo',
        'Valdepiélagos',
        'Valdetorres de Jarama',
        'Valdilecha',
        'Valverde de Alcalá',
        'Velilla de San Antonio',
        'Vellón (El)',
        'Venturada',
        'Villaconejos',
        'Villa del Prado',
        'Villalbilla',
        'Villamanrique de Tajo',
        'Villamanta',
        'Villamantilla',
        'Villanueva de la Cañada',
        'Villanueva del Pardillo',
        'Villanueva de Perales',
        'Villar del Olmo',
        'Villarejo de Salvanés',
        'Villaviciosa de Odón',
        'Villavieja del Lozoya',
        'Zarzalejo',
        'Lozoyuela-Navas-Sieteiglesias',
        'Puentes Viejas',
        'Tres Cantos',        
    ];
}

export function validadorMultiplesRangosEdad(listadoDeRangos: FormGroup[]) {
    function getEdadInicio(rango: any) {
        switch(rango.tipo){
            case Globales.TIPOS_RANGOS_EDAD.MENOR_QUE: return null;
            case Globales.TIPOS_RANGOS_EDAD.ENTRE: return rango.edadInicio;
            case Globales.TIPOS_RANGOS_EDAD.MAYOR_QUE: return rango.edad;
            case Globales.TIPOS_RANGOS_EDAD.TODOS: return null;
            default: return undefined;
        }
    }

    function getEdadFin(rango: any) {
        switch(rango.tipo){
            case Globales.TIPOS_RANGOS_EDAD.MENOR_QUE: return rango.edad;
            case Globales.TIPOS_RANGOS_EDAD.ENTRE: return rango.edadFin;
            case Globales.TIPOS_RANGOS_EDAD.MAYOR_QUE: return null;
            case Globales.TIPOS_RANGOS_EDAD.TODOS: return null;
            default: return undefined;
        }
    }
    
    if(listadoDeRangos) {
        let edadFinDeUltimoHorario = 0;
        const rangoErroneo: FormGroup = listadoDeRangos?.find((rango: FormGroup, indice: number) => {
            let edadInicio = parseInt(getEdadInicio(rango.value));
            let edadFin = parseInt(getEdadFin(rango.value));
            if(indice > 0 && (!edadFinDeUltimoHorario || edadInicio <= edadFinDeUltimoHorario)) {
                return true;
            }
            edadFinDeUltimoHorario = edadFin;
            return false;
        })
        return rangoErroneo ? {forbiddenRange: {value: listadoDeRangos.findIndex((rango: FormGroup) => rango === rangoErroneo)}} : null ;
    }
    return {forbiddenRange: {value: null}};
}

export function validadorFecha(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if(control.value) {
        const regEx: RegExp = new RegExp("^([0]?[1-9]|[1|2][0-9]|[3][0|1])[./-]([0]?[1-9]|[1][0-2])[./-]([0-9]{4}|[0-9]{2})$");
        const valida = regEx.test(control.value.format(Globales.FORMATO_FECHA_NACIONAL));
        return valida ? null : {forbiddenDate: {value: control.value.format(Globales.FORMATO_FECHA_NACIONAL)}};
      }
      return {forbiddenDate: {value: null}};
    };
}

export function validadorHoraInicioyFin(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if(control.value) {
            /*Este método solo funciona si las horas se guardan en formato 24h*/
            let valida = control.value.horaInicio < control.value.horaFin;
            return valida ? null : {forbiddenTime: {value1: control.value.horaInicio, value2: control.value.horaFin}};
        }
        return {forbiddenTime: {value: null}};
    };
}

export function validadorMultiplesHorarios(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if(control.value) {
            /*Este método solo funciona si las horas se guardan en formato 24h*/
            const listadoDeHorarios: any[] = control.value;
            let horaFinDeUltimoHorario = '';
            const horarioErroneo = listadoDeHorarios?.find((horario: any) => {
                if(horario.horaInicio && horario.horaInicio.toTimeString().substring(0,8) < horaFinDeUltimoHorario) {
                    return true;
                } else {
                    horaFinDeUltimoHorario = horario.horaFin ? horario.horaFin.toTimeString().substring(0,8) : '';
                    return false;
                }
            })
            return horarioErroneo ? {forbiddenTime: {value: listadoDeHorarios.findIndex((horario) => horario === horarioErroneo)}} : null ;
        }
        return {forbiddenTime: {value: null}};
    };
}

export function esTextoMasGrandeQueContenedor(texto: any):boolean {return texto.scrollWidth <= texto.clientWidth}
export function convertirListaAString(lista: any[]):string {
    let resultado = "";
    lista.forEach((elemento: any, indice: number) => resultado += indice === 0 ? elemento : ":" + elemento);
    return resultado;
}

export function formatearDatosImagenAArchivo(imagen: any) {
    const urlBase64 = "data:"+imagen.tipo+";base64,"+imagen.datosImagen;
    const nombreArchivo = imagen.nombre;
    var arr = urlBase64.split(','),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = window.atob(arr[1]), 
        n = bstr.length, 
        u8arr = new Uint8Array(n);
    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], nombreArchivo, {type:mime});
  }

