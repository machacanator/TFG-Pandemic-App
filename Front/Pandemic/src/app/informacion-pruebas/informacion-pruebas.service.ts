import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { retry, firstValueFrom } from 'rxjs';
import Globales from 'src/constantes/globales';
import UrlAplicacion from 'src/constantes/urls';

@Injectable({
  providedIn: 'root'
})
export class ServicioInformacionPruebas {

  constructor(private http: HttpClient) { }

  getInformacionPruebasDisponibles() {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.PRUEBAS_COMPLEMENTARIAS}/informacion`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }
}
