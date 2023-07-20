import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, retry } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioDocumentos {

  constructor(private http: HttpClient) { }

  getAvisoLegal() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.DOCUMENTOS}/aviso-legal`, {observe: 'response', responseType: 'blob' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getTerminosYCondiciones() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.DOCUMENTOS}/terminos-y-condiciones`, {observe: 'response', responseType: 'blob' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
