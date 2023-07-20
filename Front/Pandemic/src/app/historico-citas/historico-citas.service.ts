import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioHistoricoCitas {

  constructor(private http: HttpClient) {}

  getHistoricoDeCitas(modo: string, indicePagina: number, resultadosPorPagina: number) {
    console.log(modo)
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CITAS}/historico?modo=${modo}&pagina=${indicePagina}&resultados=${resultadosPorPagina}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
