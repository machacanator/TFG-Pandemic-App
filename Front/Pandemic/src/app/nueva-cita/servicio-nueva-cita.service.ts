import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatCalendar } from '@angular/material/datepicker';
import { firstValueFrom, retry, Subject } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioNuevaCita {

  cambioCabeceraCalendario = new Subject<any>();

  constructor(private http: HttpClient) { } 

  getCentrosConPruebaDisponible(idPrueba: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CENTROS}/prueba/${idPrueba}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getPruebasDisponiblesUsuario() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.PRUEBAS_COMPLEMENTARIAS}/paciente`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getDiasDisponiblesCentro(prueba: number, centro: number, anno: number, mes: number) {
    let json: Object = {
      prueba,
      anno,
      mes,
    }
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.CENTROS}/${centro}/dias`, json).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getHorasDisponiblesCentro(prueba: number, centro: number, fecha: string) {
    let json: Object = {
      prueba,
      fecha,
    }
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.CENTROS}/${centro}/horas`, json).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getInfoCita(idCita: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CITAS}/${idCita}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getInfoPrueba(idPrueba: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.PRUEBAS_COMPLEMENTARIAS}/${idPrueba}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  pedirCita(formulario: any) {
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.CITAS}/pedirCita`, formulario, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  modificarCita(formulario: any) {
    return firstValueFrom(
      this.http.patch(`${UrlAplicacion.CITAS}/cambiarCita`, formulario, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
