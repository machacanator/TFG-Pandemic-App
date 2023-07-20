import { Component, ViewEncapsulation } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { delay } from 'rxjs/operators';
import Globales from 'src/constantes/globales';
import { ServicioCargando } from './cargando/servicio-cargando.service';
import { ServicioAplicacion } from './servicio-app.service';

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Pandemic';
  mostrarBarras = false;
  menuDesplegableActivado = false;
  estaCargando = false;

  constructor(
    private enrutador: Router,
    private servicioCargando: ServicioCargando,
  ) {
    enrutador.events.forEach((evento) => {
      if (evento instanceof NavigationStart) {
        this.mostrarBarras = (evento.url !== '/iniciarSesion' && ServicioAplicacion.estaLogueado());
      }
    });
  }

  ngOnInit() {
    this.comprobarSiEstaCargando();
  }

  private comprobarSiEstaCargando(): void {
    this.servicioCargando.observableCargandoURL
      .pipe(delay(0)) // This prevents a ExpressionChangedAfterItHasBeenCheckedError for subsequent requests
      .subscribe((loading) => {
        this.estaCargando = loading;
      });
  }

  clickMenu() {
    this.menuDesplegableActivado = !this.menuDesplegableActivado;
    if (this.menuDesplegableActivado) {
      document.body?.classList.add("sin-scroll");
      document.querySelector('.icono-menu')?.classList.add("icono-menu--modo-menu-desplegable");
      document.querySelector('.logo')?.classList.add("logo--modo-menu-desplegable");
      document.querySelector('.icono-pandemic')?.classList.add("icono-pandemic--modo-menu-desplegable");
    }
  }

  clickLogo() {
    if(!this.menuDesplegableActivado) this.enrutador.navigate(['']);
  }

  cambiarIconos() {
    document.body?.classList.remove("sin-scroll");
    document.querySelector('.icono-menu')?.classList.remove("icono-menu--modo-menu-desplegable");
    document.querySelector('.logo')?.classList.remove("logo--modo-menu-desplegable");
    document.querySelector('.icono-pandemic')?.classList.remove("icono-pandemic--modo-menu-desplegable");
    this.menuDesplegableActivado = false;
  }
}
