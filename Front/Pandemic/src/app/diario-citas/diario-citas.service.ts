import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioDiarioCitas {

  constructor(private http: HttpClient) {}

  getDiarioCitas(indicePagina: number, resultadosPorPagina: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CITAS}/diario?pagina=${indicePagina}&resultados=${resultadosPorPagina}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
