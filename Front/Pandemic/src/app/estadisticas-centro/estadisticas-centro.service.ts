import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioEstadisticasCentro {

  constructor(private http: HttpClient) { }

  getNombreCentro() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_CENTRO}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
  
  getEmpleadosCentro() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getDatosGraficosEstadisticasEmpleado(idUsuario: number, fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}?usuario=${idUsuario}&fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getDatosGraficosEstadisticasTodasCarpetasCentroAdministrador(fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}/centro/carpetas-pruebas?fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getDatosGraficosEstadisticasDeCarpetaCentroAdministrador(idCarpeta:number|string, fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}/centro/pruebas?id=${idCarpeta}&fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getDatosGraficosEstadisticasCarpetasCentro(idCentro: number, fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}/centro/carpetas-pruebas?centro=${idCentro}&fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getDatosGraficosEstadisticasCarpetaCentro(idCentro: number, idCarpeta: number, fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}/centro/pruebas?centro=${idCentro}&id=${idCarpeta}&fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
