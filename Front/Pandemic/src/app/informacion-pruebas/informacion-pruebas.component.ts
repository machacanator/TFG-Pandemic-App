import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import Errores from 'src/constantes/errores';
import Globales from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioInformacionPruebas } from './informacion-pruebas.service';

@Component({
  selector: 'app-informacion-pruebas',
  templateUrl: './informacion-pruebas.component.html',
  styleUrls: ['./informacion-pruebas.component.scss']
})
export class InformacionPruebas implements OnInit {
  datosPrueba: any;
  anchuraVentana: number;
  selectorPrueba: FormControl;
  listadoPruebasDisponibles: any[] = [];

  constructor(
    private servicioPopUp: ServicioPopUp,
    private servicioInformacionPruebas: ServicioInformacionPruebas,
  ) {
    this.selectorPrueba = new FormControl({value: '', disabled: false}, [Validators.required, Validators.min(1)]);
    this.iniciarListadoDePruebasDisponibles();
  }

  private iniciarListadoDePruebasDisponibles() {
    this.servicioInformacionPruebas.getInformacionPruebasDisponibles()
      .then((listadoPruebas: any) => {this.listadoPruebasDisponibles = listadoPruebas; console.log(this.listadoPruebasDisponibles)}) 
      .catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_COMPLEMENTARIAS));
  }

  ngOnInit(): void {
    this.cambioAnchuraVentana();
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }

  cargarInformacionPrueba() {
    this.selectorPrueba.markAsTouched()
    if(this.selectorPrueba.valid) {
      let desplegableInfoPrueba = document.querySelector('.conjunto-desplegables');
      desplegableInfoPrueba?.classList.add("conjunto-desplegables--ocultar");
      setTimeout(() => {
        this.datosPrueba = this.selectorPrueba.value;
        desplegableInfoPrueba?.classList.remove("conjunto-desplegables--ocultar");
      }, 700); 
    } 
  }


}
