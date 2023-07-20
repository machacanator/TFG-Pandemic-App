import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import Errores from 'src/constantes/errores';
import Globales from 'src/constantes/globales';
import { ServicioCargando } from '../cargando/servicio-cargando.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';


@Injectable({
  providedIn: 'root',
})
export default class InterceptorTokens implements HttpInterceptor {

  constructor(
    private servicioPopUp: ServicioPopUp,
    private servicioCargando: ServicioCargando,
    private servicioAplicacion: ServicioAplicacion,
  ) {}

  private getJSONErrorPorDefecto(error: any) { //Si no es un error manejado por el back
    return throwError(() => {
      return {
        ...error,
        mensajeErrorPorDefecto: error.status+": "+error.message,
      };
    });
  }
  
  private getJSONErrorControlado(error: any) { //Si es un error manejado por el back
    let jsonError = error.error instanceof Object ? error.error : JSON.parse(error.error);
    let mensajeError = jsonError[Globales.RESPUESTA.PARAMETRO_ERROR_CODIGO]+": "+jsonError[Globales.RESPUESTA.PARAMETRO_ERROR_MENSAJE];
    if (jsonError[Globales.RESPUESTA.PARAMETRO_ERROR_CODIGO] === Errores.CODIGOS_BACK.TOKEN_EXPIRADO.codigo) {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.CODIGOS_BACK.TOKEN_EXPIRADO.mensaje);
    }
    if(jsonError[Globales.RESPUESTA.PARAMETRO_ERROR_CODIGO] === Errores.CODIGOS_BACK.AUTENTICACION.codigo) {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.CODIGOS_BACK.AUTENTICACION.mensaje);
    }
    return throwError(() => {
      return {
        ...jsonError,
        mensajeErrorPorDefecto: mensajeError,
      };
    });
  }


  intercept(peticionHttp: HttpRequest<any>, manejadorPeticiones: HttpHandler): Observable<HttpEvent<any>> {
    this.servicioCargando.setCargando(true, peticionHttp.url);
    const token = localStorage.getItem('token');
    let peticion = peticionHttp;

    if (token) {
      peticion = peticionHttp.clone({
        setHeaders: {
          tokenAcceso: token,
        },
      });
    }

    return manejadorPeticiones.handle(peticion).pipe(catchError((error) => {
      this.servicioCargando.setCargando(false, peticion.url);
      //Si no esta el usuario autorizado lo fuerza a loguear de nuevo
      if (error.status === 401) { 
        this.servicioAplicacion.cerrarSesion();
      } 
      return error.error ? this.getJSONErrorControlado(error) : this.getJSONErrorPorDefecto(error);
    }))
      .pipe(map<HttpEvent<any>, any>((evento: HttpEvent<any>) => {
        if (evento instanceof HttpResponse) {
          this.servicioCargando.setCargando(false, peticion.url);
        }
        return evento;
      }));
  }
}