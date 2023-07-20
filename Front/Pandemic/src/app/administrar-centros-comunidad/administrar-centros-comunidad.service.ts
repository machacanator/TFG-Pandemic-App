import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioAdministrarCentrosComunidad {

  constructor( private http:HttpClient) { }
  
  getNombresAdministradoresCentro(idCentro: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/${idCentro}/nombres-administradores`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getAdministradoresCentro(idCentro: number | string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/${idCentro}/administradores`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getInfoAdministrador(numeroSeguridadSocial: string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/info-persona?numeroSeguridadSocial=${numeroSeguridadSocial}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  annadirNuevoCentro(json: any) {
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/annadir-centro`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  actualizarCentro(idCentro: number | string, json: any) {
    return firstValueFrom(
      this.http.patch(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/${idCentro}/actualizar-centro`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
  
  eliminarCentro(idCentro: number) {
    return firstValueFrom(
      this.http.delete(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/eliminar?id=${idCentro}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getImagenesDeCentro(idCentro: number | string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/${idCentro}/imagenes`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  actualizarImagenDeCentro(idCentro: number | string, imagenesCentro: File[]) {
    const formData = new FormData();
    imagenesCentro.forEach((imagen: File, indice: number) => formData.append(imagen.name, imagen));
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.ADMINISTRACION_CENTROS_COMUNIDAD}/${idCentro}/imagenes`, formData, { responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
