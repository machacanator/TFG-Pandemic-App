import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import * as moment from 'moment';
import Globales from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioNotificaciones } from './servicio-notificaciones.service';

@Component({
  selector: 'app-notificaciones',
  templateUrl: './notificaciones.component.html',
  styleUrls: ['./notificaciones.component.scss']
})
export class Notificaciones implements OnInit {

  fechaHoy: string;
  fechaAyer: string;
  notificacionesAMostrar: any[];
  todasLasNotificaciones: any[];
  formularioFiltro: FormGroup;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioNotificaciones:  ServicioNotificaciones,
  ) {
    this.cargarListadoNotificaciones();
    this.iniciarFormulario();
    this.fechaHoy = moment().format(Globales.FORMATO_FECHA_NACIONAL);
    this.fechaAyer = moment().subtract(1, "days").format(Globales.FORMATO_FECHA_NACIONAL);
  }

  private cargarListadoNotificaciones() {
    console.log(this.servicioAplicacion.notificaciones)
    this.todasLasNotificaciones = this.servicioAplicacion.notificaciones ? this.agruparNotificacionesPorFecha(this.servicioAplicacion.notificaciones) : [];
    // this.todasLasNotificaciones.forEach((notificacion, i) => this.todasLasNotificaciones[i].push(notificacion[0]))
    this.notificacionesAMostrar = this.todasLasNotificaciones;
    console.log(this.todasLasNotificaciones);
  }

  private iniciarFormulario() {
    this.formularioFiltro = new FormGroup({
      filtroTexto: new FormControl({value: '', disabled: false}, [Validators.maxLength(50)]),
      filtroTiempo: new FormControl({value: 0, disabled: false}, [Validators.min(0), Validators.max(5)]),
    });
  }

  private agruparNotificacionesPorFecha(listaNotificaciones: any[]) {
    let resultado: any[] = [];
    let nuevoGrupoNotificaciones: any[] = [];

    listaNotificaciones.forEach(notificacion => {
      if (nuevoGrupoNotificaciones.length > 0) {
        if (nuevoGrupoNotificaciones[0].fecha === notificacion.fecha) {
          nuevoGrupoNotificaciones.push(notificacion);
        } else {
          resultado.push(nuevoGrupoNotificaciones);
          nuevoGrupoNotificaciones = [notificacion];
        }
      } else {
        nuevoGrupoNotificaciones.push(notificacion);
      }
    });
    if (nuevoGrupoNotificaciones.length > 0) resultado.push(nuevoGrupoNotificaciones);
    return resultado;
  }

  
  clickBotonNotificacion(direccion: String) {
    this.enrutador.navigate([direccion]);
  }
  
  // formatearFecha(fecha: Date) {
  //   return fecha.toLocaleDateString('es-ES');
  // }
  
  buscarHTMLNotificacion(elementoHTML: any, buscarHaciaArriba: boolean): any {
    if(!elementoHTML) return null;
    if (elementoHTML.classList?.contains("listado-notificaciones__notificacion")) {
      return elementoHTML
    } else {
      return buscarHaciaArriba
        ? this.buscarHTMLNotificacion(elementoHTML.parentNode, buscarHaciaArriba)
        : this.buscarHTMLNotificacion(elementoHTML.children, buscarHaciaArriba);
    }
  }


  mostrarOcultarAccionesNotificacion(htmlNotificacion: any, notificacion: any) {
    if (htmlNotificacion?.classList?.contains('mostrar-una-accion') || htmlNotificacion?.classList?.contains('mostrar-dos-acciones')) {
      htmlNotificacion.classList.remove(notificacion.visualizada ? "mostrar-una-accion" : "mostrar-dos-acciones")
    } else {
      htmlNotificacion?.classList?.add(notificacion.visualizada ? "mostrar-una-accion" : "mostrar-dos-acciones")
    }
  }

  visualizarNotificacion(htmlAccion: any, notificacion: any) {
    this.mostrarOcultarAccionesNotificacion(this.buscarHTMLNotificacion(htmlAccion, false), notificacion);
    
    // Espera hasta que termine la animacion de cierre de la notificacion
    setTimeout(()=>{this.servicioNotificaciones.marcarNotificacionVisualizada(notificacion.id)
      .then((data: any) => {
        notificacion.visualizada = true;
        this.todasLasNotificaciones.forEach(seccion => {
          let infoNotificacion = seccion.find((notif: any) => notif.id === notificacion.id);
          if(infoNotificacion) {
            infoNotificacion.visualizada = true;
            return;
          }
        });
        // this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, data);
      }).catch((error) => {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, error?.mensaje);
      });
    }, 0.7*Globales.SEGUNDOS);
  }

  borrarNotificacion(htmlAccion: any, notificacion: any) {
    this.mostrarOcultarAccionesNotificacion(this.buscarHTMLNotificacion(htmlAccion, false), notificacion);
    
    // Espera hasta que termine la animacion de cierre de la notificacion
    setTimeout(()=>{this.servicioNotificaciones.borrarNotificacion(notificacion.id)
      .then((data: any) => {
        htmlAccion.parentNode.classList.add("listado-notificaciones__acciones--desvanecer");
        // this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, data);

        // Espera hasta que termine la animacion de desvanecer de la notificacion
        setTimeout(()=>{
          this.todasLasNotificaciones.forEach((seccion, indiceSeccion) => {
            let indiceNotificacionSeccion = seccion.findIndex((notif: any) => notif.id === notificacion.id);
            if(indiceNotificacionSeccion >= 0) {
              seccion.splice(indiceNotificacionSeccion, 1);
              if(seccion.length === 0) this.todasLasNotificaciones.splice(indiceSeccion, 1);
              return;
            }
          });
        }, 0.7*Globales.SEGUNDOS);
      }).catch((error) => {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, error?.mensaje);
      });
    }, 0.7*Globales.SEGUNDOS);
  }


  ngOnInit(): void {
  }



}
