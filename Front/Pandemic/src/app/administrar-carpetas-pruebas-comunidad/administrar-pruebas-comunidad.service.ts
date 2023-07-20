import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioAdministrarPruebasComunidad {

  constructor(private http: HttpClient) { }

  getCarpetasDePruebasDeLaComunidad(indicePagina: number, resultadosPorPagina: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_CARPETAS_DE_PRUEBAS_COMUNIDAD}?pagina=${indicePagina}&resultados=${resultadosPorPagina}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getPruebasDeLaCarpetaDePruebas(indicePagina: number, resultadosPorPagina: number, idCarpetaDePruebas: number | string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_COMUNIDAD}?idCarpeta=${idCarpetaDePruebas}&pagina=${indicePagina}&resultados=${resultadosPorPagina}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getRangosDeEdadDePrueba(idPrueba: number | string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_COMUNIDAD}/requisitos?id=${idPrueba}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  annadirCarpetaDePruebas(json: any) {
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.ADMINISTRACION_CARPETAS_DE_PRUEBAS_COMUNIDAD}/annadir-carpeta-de-pruebas`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  annadirNuevaPrueba(idCarpeta: number | string, json: any) {
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_COMUNIDAD}/annadir-prueba?idCarpeta=${idCarpeta}`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  actualizarCarpetaDePruebas(json: any) {
    return firstValueFrom(
      this.http.patch(`${UrlAplicacion.ADMINISTRACION_CARPETAS_DE_PRUEBAS_COMUNIDAD}/actualizar-carpeta-de-pruebas`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  actualizarPrueba(json: any) {
    return firstValueFrom(
      this.http.patch(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_COMUNIDAD}/actualizar-prueba`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  eliminarCarpetaDePrueba(idCarpetaDePruebas: number) {
    return firstValueFrom(
      this.http.delete(`${UrlAplicacion.ADMINISTRACION_CARPETAS_DE_PRUEBAS_COMUNIDAD}/eliminar?id=${idCarpetaDePruebas}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  eliminarPrueba(idPrueba: number) {
    return firstValueFrom(
      this.http.delete(`${UrlAplicacion.ADMINISTRACION_PRUEBAS_COMUNIDAD}/eliminar?id=${idPrueba}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
