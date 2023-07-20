import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, retry, throwError } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioSecciones {

  constructor(private http: HttpClient) {}

  getAcciones(seccion: String) {
    const url = `${UrlAplicacion.ACCIONES}/${seccion}`;
    return this.http.get<any>(url, {})
      .pipe(
        retry(Globales.PETICION_REINTENTAR),
        catchError(this.handleError),
      );
  }

  getTodasSecciones() {
    const url = UrlAplicacion.SECCIONES;
    return this.http.get<any>(url, {})
      .pipe(
        retry(Globales.PETICION_REINTENTAR),
        catchError(this.handleError),
      );
  }

  private handleError(error: HttpErrorResponse) { // CAMBIAR EL METODO
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      console.error(
        `Backend returned code ${error.status}, `
        + `body was: ${error.error}`,
      );
      console.log(error);
    }
    return throwError(
      'Something bad happened; please try again later.',
    );
  }
}
