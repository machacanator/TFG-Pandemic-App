import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { formatearDatosImagenAArchivo } from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioMiCentro } from './mi-centro.service';

@Component({
  selector: 'app-mi-centro',
  templateUrl: './mi-centro.component.html',
  styleUrls: ['./mi-centro.component.scss',]
})
export class MiCentro implements OnInit {
  anchuraVentana: number;
  infoCentro: any;
  listadoImagenesCentro: any = [];

  constructor(
    private desinfectante: DomSanitizer,
    private servicioPopUp: ServicioPopUp,
    private servicioMiCentro: ServicioMiCentro,
  ) { }

  private cargarDatosCentro() {
    this.servicioMiCentro.getInformacionMiCentro()
      .then((informacion) => {this.infoCentro = informacion;})
      .catch(() => {this.infoCentro = null; this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_CENTRO)})
    this.servicioMiCentro.getImagenesDeMiCentro()
      .then((imagenesCentro: any) => {
          this.listadoImagenesCentro = imagenesCentro.map((imagen: any) => this.desinfectante.bypassSecurityTrustUrl(window.URL.createObjectURL(formatearDatosImagenAArchivo(imagen)))
      );
          // this.listadoImagenesCentro = imagenesCentro.map((imagen: any) => {
          //   const archivo = formatearDatosImagenAArchivo(imagen);
          //   return {
          //     archivo,
          //     url: this.desinfectante.bypassSecurityTrustUrl(window.URL.createObjectURL(archivo)),
          //   };
          // });
        })
      .catch(() => {this.listadoImagenesCentro = []; this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_SOLICITAR_IMAGENES_CENTRO)});
  }

  ngOnInit(): void {
    this.cambioAnchuraVentana();
    this.cargarDatosCentro();
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }
}
