import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioSesion {

  constructor(private http: HttpClient) { }

  iniciarSesion(credenciales: any) {
    return this.http.post<any>(
      UrlAplicacion.INICIO_SESION,
      new URLSearchParams(credenciales),
      {
        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
        observe: 'response',
      }
    ).pipe(
      retry(Globales.PETICION_REINTENTAR),
      //catchError()
    );
  }

  refrescarToken() {
    return this.http.get<any>(UrlAplicacion.REFRESCAR_TOKEN, {observe: 'response'}).pipe(
      retry(Globales.PETICION_REINTENTAR),
      //catchError()
    );
  }
}
