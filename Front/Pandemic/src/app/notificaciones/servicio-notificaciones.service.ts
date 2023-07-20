import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';

@Injectable({
  providedIn: 'root'
})
export class ServicioNotificaciones {

  constructor(private http: HttpClient) { }

  marcarNotificacionVisualizada(idNotificacion: number) {
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.NOTIFICACIONES}/marcar/${idNotificacion}`, {}, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
  
  borrarNotificacion(idNotificacion: number) {
    return firstValueFrom(
      this.http.delete(`${UrlAplicacion.NOTIFICACIONES}/borrar/${idNotificacion}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
