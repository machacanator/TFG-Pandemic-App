import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import Globales from 'src/constantes/globales';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioSecciones } from './servicio-permisos.service';

@Injectable({
  providedIn: 'root'
})
export class ControladorPermisos implements CanActivate {
  constructor(
    private enrutador: Router,
    private servicioSecciones: ServicioSecciones,
    private servicioAplicacion: ServicioAplicacion,
  ) {}

  async canActivate(
    ruta: ActivatedRouteSnapshot,
    estado: RouterStateSnapshot,
  ) {
    if (ServicioAplicacion.estaLogueado()) {
      let rutaActual: string = ruta.routeConfig?.path;
      if (rutaActual === '' || rutaActual === 'iniciarSesion') return true;
      if (rutaActual === 'historico-citas') rutaActual = rutaActual + '-' + window.localStorage.getItem('modo-historico');
      if (rutaActual) {
        await this.servicioSecciones.getTodasSecciones().subscribe(secciones => {
          let seccionActual: any;
          secciones.forEach((seccion: any) => {
            if (rutaActual === Globales.MENU_OPCIONES[seccion.id].url) {
              seccionActual = seccion;
              return;
            }
            seccion.subSecciones.forEach((subSeccion: any) => {
              if (rutaActual === Globales.MENU_OPCIONES[subSeccion.id].url) {
                seccionActual = subSeccion;
                return;
              }
            });
          });
          // response.data.forEach((item) => {
          //   item.sectionSubSectionsList.forEach(section => {
          //     if (rutaActual === AppConstants.MENU_OPTIONS[section.idSection].url) {
          //       seccionActual = section;
          //       return;
          //     }
          //     section.subSections.forEach(sub => {
          //       if (rutaActual === AppConstants.MENU_OPTIONS[sub.id].url) {
          //         seccionActual = sub;
          //       }
          //     });
          //   });
          // });
          this.servicioSecciones.getAcciones(seccionActual.id).subscribe(acciones => {
            this.servicioAplicacion.setAcciones(acciones);
          });
        });
        return true;
      }
    }
    this.enrutador.navigate(['iniciarSesion']);
    return false;
  }
}
