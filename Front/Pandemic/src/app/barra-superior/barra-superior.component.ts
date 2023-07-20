import { Component, Input, OnChanges, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import Globales from 'src/constantes/globales';
import { ServicioAplicacion } from '../servicio-app.service';

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'barra-superior',
  templateUrl: './barra-superior.component.html',
  styleUrls: ['./barra-superior.component.scss'],
})
export class BarraSuperior implements OnInit, OnChanges {

  @Input() mostrarCabecera: boolean = false;

  nombreUsuario: string;
  hayNuevasNotificaciones: boolean;
  avisoMostradoPreviamente: boolean;
  modoSimplificado: boolean = false;
  mostrarMenuUsuario: boolean = false;
  mostrarAvisoNuevasNotificaciones: boolean = false;

  // Constantes para timeouts
  funcionMostrarAviso: any;
  funcionCerrarAviso: any;
  funcionNoRepetirAviso: any;

  constructor(
    private enrutador: Router,
    private servicioAplicacion: ServicioAplicacion,
    ) {}

  ngOnInit(): void {
      this.cambioAnchuraVentana();
  }

  ngOnChanges(): void {
    this.mostrarMenuUsuario = false;
    this.mostrarAvisoNuevasNotificaciones = false;
    this.nombreUsuario = this.servicioAplicacion.getNombreUsuario();
    this.hayNuevasNotificaciones =  this.servicioAplicacion.notificaciones?.filter(notificacion => !notificacion.visualizada).length > 0;
    this.avisoMostradoPreviamente = JSON.parse(localStorage.getItem('avisoNotificacionesYaHaSidoMostrado'));
    if (!this.avisoMostradoPreviamente && this.hayNuevasNotificaciones && localStorage.getItem('token')) {
      this.mostrarAvisoNotificaciones();
      this.ocultarAvisoNotificaciones();
    }
  }

  private mostrarAvisoNotificaciones() {
    // Es necesario cambiar la variable con delay si queremos que se aplique la propiedad "transition"
    this.funcionMostrarAviso = setTimeout(() => {
      this.mostrarAvisoNuevasNotificaciones = true;
      let sonidoCampana =  new Audio();
      sonidoCampana.src = "../../assets/AudioNotificaciones/sonido-campana.wav";
      sonidoCampana.load();
      sonidoCampana.play();
    }, 1 * Globales.SEGUNDOS);
    // Ejecuta la animacion de cierre del aviso
    this.funcionCerrarAviso = setTimeout(() => {this.mostrarAvisoNuevasNotificaciones = false}, 5 * Globales.SEGUNDOS);
  }

  private ocultarAvisoNotificaciones() {
    // Tras acabar la animacion de cierre del aviso desactivamos el aviso
    // para no interferir con el contenido de la pagina
    this.funcionNoRepetirAviso = setTimeout(() => {
      this.avisoMostradoPreviamente = true;
      localStorage.setItem('avisoNotificacionesYaHaSidoMostrado', 'true');
    }, 6 * Globales.SEGUNDOS);
  }

  clickNotificaciones() {
    this.hayNuevasNotificaciones = false;
    // Reiniciamos la variable para mostrar el aviso de nuevo en caso de que lleguen nuevas notificaciones
    localStorage.setItem('avisoNotificacionesYaHaSidoMostrado', 'false');
    this.enrutador.navigate(['notificaciones']);
  }

  cerrarSesion() {
    this.mostrarMenuUsuario = false;
    clearTimeout(this.funcionMostrarAviso);
    clearTimeout(this.funcionCerrarAviso);
    clearTimeout(this.funcionNoRepetirAviso);
    this.servicioAplicacion.cerrarSesion();
  }

  cambioAnchuraVentana() {
    this.modoSimplificado = document.body.clientWidth <= 750;
  }

}
