import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioCitasPendientes {

  constructor(private http: HttpClient) {}

  getCitasPendientes(indicePagina: number, resultadosPorPagina: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CITAS}/pendientes?pagina=${indicePagina}&resultados=${resultadosPorPagina}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  eliminarCita(idCita: number) {
    return firstValueFrom(
      this.http.delete(`${UrlAplicacion.CITAS}/eliminar?id=${idCita}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
