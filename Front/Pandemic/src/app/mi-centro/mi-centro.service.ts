import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioMiCentro {

  constructor(private http: HttpClient) { }

  getInformacionMiCentro() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CENTROS}/mi-centro`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getImagenesDeMiCentro() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.CENTROS}/mi-centro/imagenes`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
