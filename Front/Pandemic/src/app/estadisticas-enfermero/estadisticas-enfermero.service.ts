import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry, firstValueFrom } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioEstadisticasEnfermero {

  constructor(private http: HttpClient) { }

  getDatosGraficosEstadisticas(fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}?fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
