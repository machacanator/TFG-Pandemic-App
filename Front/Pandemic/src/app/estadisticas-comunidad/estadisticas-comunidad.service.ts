import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry, firstValueFrom } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServiciosEstadisticasComunidad {

  constructor(private http: HttpClient) { }

  getDatosGraficosEstadisticasCentrosComunidad(fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}/comunidad?fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getDatosGraficosEstadisticasCarpetaComunidad(idCarpeta: number, fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}/comunidad/carpetas-pruebas?id=${idCarpeta}&fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
  
  getDatosGraficosEstadisticasPruebaComunidad(idPrueba: number, fecha: string, modoAnual: boolean) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ESTADISTICAS}/comunidad/pruebas?id=${idPrueba}&fecha=${fecha}&modoAnual=${modoAnual}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
