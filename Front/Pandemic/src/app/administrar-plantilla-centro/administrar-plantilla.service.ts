import { HttpClient } from '@angular/common/http';
import { firstValueFrom, retry } from 'rxjs';
import { Injectable } from '@angular/core';
import UrlAplicacion from 'src/constantes/urls';
import Globales from 'src/constantes/globales';

@Injectable({
  providedIn: 'root'
})
export class ServicioAdministrarPlantilla {

  constructor(private http: HttpClient) { }

  getPlantillaDeCentro(indicePagina: number, resultadosPorPagina: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}?pagina=${indicePagina}&resultados=${resultadosPorPagina}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  getPruebasQuePuedeTratarEmpleado(idEmpleado: number) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}/pruebas-empleado?id=${idEmpleado}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  eliminarEmpleado(idEmpleado: number) {
    return firstValueFrom(
      this.http.delete(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}/eliminar?id=${idEmpleado}`, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  buscarInformacionEmpleadoPorId(idEmpleado: string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}/empleado?id=${idEmpleado}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  buscarInformacionCandidatoPorSeguridadSocial(seguridadSocialEmpleado: string) {
    return firstValueFrom(
      this.http.get(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}/candidato?numeroSeguridadSocial=${seguridadSocialEmpleado}`).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  annadirCandidatoAPlantilla(json: any) {
    return firstValueFrom(
      this.http.post(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}/annadir-candidato`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  actualizarEmpleado(json: any) {
    return firstValueFrom(
      this.http.patch(`${UrlAplicacion.ADMINISTRACION_PLANTILLA}/actualizar-empleado`, json, {responseType: 'text' as 'json'}).pipe(
        retry(Globales.PETICION_REINTENTAR),
        // catchError(),
      )
    );
  }

  
}
