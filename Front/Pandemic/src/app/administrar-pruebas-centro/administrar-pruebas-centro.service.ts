import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioAdministrarPruebasCentro {

  constructor(private http:HttpClient) { }

  getPruebasQuePuedeTratarCentroDeAdmin() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_CENTRO}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getHorariosPruebaCentroDeAdmin(idPrueba: number | string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_CENTRO}/${idPrueba}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
  
  getPruebasYHorariosCentroDeAdmin(indicePagina: number, resultadosPorPagina: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_CENTRO}/horarios?pagina=${indicePagina}&resultados=${resultadosPorPagina}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  eliminarPruebaDeCentro(idPrueba: number) {
    return firstValueFrom(
      this.http.delete(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_CENTRO}/eliminar?id=${idPrueba}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  annadirNuevaPruebaACentro(json: any) {
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_CENTRO}/annadir-prueba`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  actualizarHorariosPruebaCentro(json: any) {
    return firstValueFrom(
      this.http.patch(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_CENTRO}/actualizar-horarios-prueba`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
