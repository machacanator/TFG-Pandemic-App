import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, retry, firstValueFrom, Observable } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';
import jwt_decode from 'jwt-decode';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import { ServicioPopUp } from './popup/servicio-pop-up.service';

@Injectable({
  providedIn: 'root'
})
export class ServicioAplicacion {
  public roles: string[];
  public seccionesMenu: any[];
  public acciones: any[];
  public notificaciones: any[];
  public nombreCompletoUsuario: string;
  public fechaExpiracion: Date;
  public listadoPruebas: any[] = [];
  public listadoCarpetasPruebas: any[] = [];
  public listadoCentros: any[] = [];

  constructor(
    private http: HttpClient,
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
  ) {
    this.acciones = [];
    this.nombreCompletoUsuario = "";
    if (!JSON.parse(localStorage.getItem('avisoNotificacionesYaHaSidoMostrado'))) {
      localStorage.setItem('avisoNotificacionesYaHaSidoMostrado', "false");
    }
  }

  cargarDatos() {
    const token: string = localStorage.getItem('token');
    console.log('TOKEN', token)
    if(token) {
      /*Precargar datos*/
      this.procesarToken(token);
      const promesaNombreCompletoUsuario: Promise<any> = this.getNombre();
      const promesaSecciones: Promise<any> = this.getSecciones();
      const promesaNotificaciones: Promise<any> = this.getNotificaciones();
      const promesaCentros: Promise<any> = this.getCentros();
      const promesaPruebasComplementarias: Promise<any> = this.getPruebasComplementarias();
      const promesaCarpetasDePruebas: Promise<any> = this.esUsuarioAdministrador() ? this.getCarpetasPruebas() : Promise.resolve();
      console.log("ROLES", this.roles)
      
      return Promise.all([
        promesaNombreCompletoUsuario,
        promesaSecciones,
        promesaNotificaciones,
        promesaCentros,
        promesaPruebasComplementarias,
      ]);
    }
    return undefined;
  }

  getNombreUsuario():string {
    if (this.nombreCompletoUsuario) {
      let nombreCompleto = this.nombreCompletoUsuario;
      let indiceComienzoNombre = nombreCompleto.indexOf(", ");
      return nombreCompleto.substring(indiceComienzoNombre + 2, nombreCompleto.length);
    } 
    return "";
  }
  
  private esUsuarioAdministrador() {
    return this.roles.some((rol: string) => rol.includes("Administrador"));
  }

  private procesarToken(token: string) {
    const informacionToken: any = jwt_decode(token);
    this.roles = informacionToken.roles;
    this.fechaExpiracion = new Date(informacionToken.exp * Globales.SEGUNDOS);
  }

  private async getNombre() {
    return firstValueFrom(
      this.http.get<any>(`${UrlAplicacion.USUARIO}/nombre`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    ).then((data: any) => {
      this.nombreCompletoUsuario = data;
    }).catch((error) => {
      return error;
    });
  }

  public async getCentros() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CENTROS}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    ).then((data: any) => {
      this.listadoCentros = data;
    }).catch((error) => {
      this.listadoCentros = [];
      this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_CENTROS);
      return error;
    });
  }

  public async getPruebasComplementarias() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.PRUEBAS_COMPLEMENTARIAS}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    ).then((data: any) => {
      this.listadoPruebas = data;
    }).catch((error) => {
      this.listadoPruebas = [];
      this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_COMPLEMENTARIAS);
      return error;
    });
  }

  private async getCarpetasPruebas() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CARPETAS_PRUEBAS}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    ).then((data: any) => {
      this.listadoCarpetasPruebas = data;
    }).catch((error) => {
      this.listadoCarpetasPruebas = [];
      this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_CARPETAS_PRUEBAS);
      return error;
    });
  }

  private async getNotificaciones() {
    return firstValueFrom(
      this.http.get<any>(`${UrlAplicacion.USUARIO}/notificaciones`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    ).then((data: any) => {
      this.notificaciones = data.map(
        (notificacion: any) => {
          return this.getNotificacionCompleta(notificacion);
        }
      );
    }).catch((error) => {
      return error;
    });
  }

  private getNotificacionCompleta(notificacion: any) {
    let datosCodigoNotificacion = Globales.CODIGOS_NOTIFICACIONES[notificacion.codigo];
    let mensaje = '';
    if (notificacion.referencia) {
      let fechaHoraReferencia = moment(notificacion.referencia.fecha);
      mensaje = datosCodigoNotificacion.mensaje.replace('###', notificacion.codigo.includes('cita')
        ? fechaHoraReferencia.format(Globales.FORMATO_HORA)+" del "+fechaHoraReferencia.format(Globales.FORMATO_FECHA_NACIONAL).replaceAll('-', '/')
        : notificacion.referencia.nombre
      )
    } else {
      mensaje = datosCodigoNotificacion.mensaje;
    }

    return {
      ...notificacion,
      ...datosCodigoNotificacion,
      fecha: moment(notificacion.fecha).format(Globales.FORMATO_FECHA_NACIONAL),
      mensaje,
    };
  }

  private async getSecciones () {
    return firstValueFrom(
      this.http.get<any>(UrlAplicacion.SECCIONES__MENU).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    ).then((data: any) => {
      this.seccionesMenu = data;
    }).catch((error) => {
      return error;
    });
  }

  static estaLogueado(): boolean {
    return !!window.localStorage.getItem('token');
  }

  cerrarSesion() { //Posiblemente haya que quitarlo
    /*this.http.delete(UrlAplicacion.INICIO_SESION, {}).subscribe(() => {
      localStorage.removeItem('token');
      this.router.navigate(['iniciarSesion']);
    });*/
    localStorage.removeItem('token');
    localStorage.setItem('avisoNotificacionesYaHaSidoMostrado', 'false');
    this.enrutador.navigate(['iniciarSesion']); 
  }

  public setAcciones(acciones: any[]) {
    this.acciones = acciones;
  }

  public setPermisos(acciones: any[]) {
    acciones?.forEach(accion => {
      if (accion === 'table') {
        const tablas = document.querySelectorAll('.tableElement');
        const listadoTablas = Array.prototype.slice.call(tablas);
        listadoTablas.forEach((instanciaTabla) => {
          const tabla = instanciaTabla;
          tabla.style.display = tabla.id === 'resolversSummary' ? 'block' : 'table';
        });
      } else {
        const botones = document.querySelectorAll(`#${accion}`);
        const listadoBotones = Array.prototype.slice.call(botones);
        listadoBotones.forEach((instanciaBoton) => {
          const boton = instanciaBoton;
          boton.style.display = 'inline-block';
        });
      }
    });
  }
}
