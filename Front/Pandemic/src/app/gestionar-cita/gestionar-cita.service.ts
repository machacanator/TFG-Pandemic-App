import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioGestionarCita {

  constructor(private http: HttpClient) { }

  getDatosPaciente(idPaciente: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.PERSONA}/${idPaciente}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getPruebasPendientes(idPaciente: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.PRUEBAS_COMPLEMENTARIAS}/pendientes/${idPaciente}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getPruebasRealizadas(idPaciente: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.PRUEBAS_COMPLEMENTARIAS}/realizadas/${idPaciente}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
  
  gestionarCita(formulario: any) {
    return firstValueFrom(
      this.http.patch(`${UrlAplicacion.CITAS}/gestionarCita`, formulario, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
