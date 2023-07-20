import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import Globales from 'src/constantes/globales';
import { ServicioNuevaCita } from '../nueva-cita/servicio-nueva-cita.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioGestionarCita } from './gestionar-cita.service';

@Component({
  selector: 'app-gestionar-cita',
  templateUrl: './gestionar-cita.component.html',
  styleUrls: ['./gestionar-cita.component.scss']
})
export class GestionarCita implements OnInit {

  datosCita: any;
  datosPaciente: any;
  listadoPruebasPendientes: string[] = [];
  listadoPruebasRealizadas: string[] = [];
  formulario: FormGroup;


  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private servicioCita: ServicioNuevaCita,
    private servicioAplicacion: ServicioAplicacion,
    private servicioGestionCita: ServicioGestionarCita,
  ) {
    this.cargarDatos();
    this.iniciarFormulario();
  }

  private iniciarFormulario() {
    this.formulario = new FormGroup({
      estado: new FormControl({value: 1, disabled: false}, [Validators.required]),
      observaciones: new FormControl({value: '', disabled: false}, [Validators.required, Validators.minLength(1)]),
    });
  }
  
  private cargarDatos() {
    this.servicioCita.getInfoCita(parseInt(window.localStorage.getItem('idCitaGestion'))).then((datosCita: any) =>  {
      let fecha = moment(datosCita.fecha);
      this.datosCita = {
        ...datosCita,
        fecha: fecha.format(Globales.FORMATO_FECHA_NACIONAL).replaceAll('-', '/'),
        hora: fecha.format(Globales.FORMATO_HORA),
        nombrePrueba: this.servicioAplicacion.listadoPruebas.find(prueba => prueba.id === datosCita.prueba).nombre,
      };
      if(this.datosCita.estado === -3)this.formulario.get("estado").setValue(this.datosCita.estado);
      this.formulario.get("observaciones").setValue(this.datosCita.observaciones);
      this.servicioGestionCita.getDatosPaciente(this.datosCita.paciente).then((datosPaciente: any) => {
        this.datosPaciente = {
          ...datosPaciente,
          edad: moment(Date.now()).diff(datosPaciente.fechaNacimiento, 'years'),
        };
        const promesaListadoPendientes = this.servicioGestionCita.getPruebasPendientes(this.datosPaciente.id);
        const promesaListadoRealizadas = this.servicioGestionCita.getPruebasRealizadas(this.datosPaciente.id);
        Promise.all([
          promesaListadoPendientes,
          promesaListadoRealizadas,
        ]).then((listados: any[]) => {
          this.listadoPruebasPendientes = listados[0];
          this.listadoPruebasRealizadas = listados[1];
        }).catch(() => {
          this.listadoPruebasPendientes = [];
          this.listadoPruebasRealizadas = [];
          this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_COMPLEMENTARIAS);
        })
      }).catch(() => {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_INFO_PACIENTE);
      });
    }).catch(() => {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_INFO_CITA);
    });
  }

  ngOnInit(): void {
  }

  volverADiarioDeCitas() {
    window.localStorage.removeItem('idCitaGestion');
    this.enrutador.navigate(['diario-citas']);
  }

  cambioAnchuraVentana() {}

  gestionarCita() {
    this.formulario.markAllAsTouched();
    if (this.formulario.valid) {
      const json = {
        id: this.datosCita.id,
        ...this.formulario.getRawValue(),
      }
      this.servicioGestionCita.gestionarCita(json).then(() => {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Errores.MENSAJES.ERROR_GESTIONAR_CITA);
        this.volverADiarioDeCitas();
      }).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_GESTIONAR_CITA));
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO)
    }
  }

}
