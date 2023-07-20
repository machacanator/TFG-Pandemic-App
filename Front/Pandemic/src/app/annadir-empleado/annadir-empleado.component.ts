import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { convertirListaAString } from 'src/constantes/globales';
import { ServicioAdministrarPlantilla } from '../administrar-plantilla-centro/administrar-plantilla.service';
import { ServicioAdministrarPruebasCentro } from '../administrar-pruebas-centro/administrar-pruebas-centro.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';

@Component({
  selector: 'app-annadir-empleado',
  templateUrl: './annadir-empleado.component.html',
  styleUrls: ['./annadir-empleado.component.scss']
})
export class AnnadirEmpleado implements OnInit {
  formulario:FormGroup;
  modoEditarEmpleado: boolean;
  listaPruebasDeCentro: any[] = [];
  listaPruebasDisponibles: any[] = [];
  mostrarInformacionEmpleado: boolean;
  informacionEmpleadoSeleccionado: any = null;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioAdministracionPlantilla: ServicioAdministrarPlantilla,
    private servicioAdministracionPruebasCentro: ServicioAdministrarPruebasCentro,
  ) {
    this.mostrarInformacionEmpleado = false;
    this.modoEditarEmpleado = !!window.localStorage.getItem('idEmpleado');
    this.iniciarFormulario();
    this.iniciarListados();
  }

  private iniciarFormulario() {
    this.formulario = new FormGroup({
      seguridadSocial: new FormControl({value: '', disabled: this.modoEditarEmpleado}, [Validators.required, Validators.minLength(9)]),
      pruebasTratables: new FormControl({value: [], disabled: false}, [Validators.required]),
    });
    
  }

  private iniciarListados() {
    let promesaInfoEmpleado = this.modoEditarEmpleado
      ? this.servicioAdministracionPlantilla.buscarInformacionEmpleadoPorId(window.localStorage.getItem('idEmpleado')).then(
          (datosEmpleado: any) => {
            this.informacionEmpleadoSeleccionado = datosEmpleado;
            this.mostrarInformacionEmpleado = true;
            this.formulario.get("seguridadSocial").setValue(this.informacionEmpleadoSeleccionado.numeroSeguridadSocial);
          }
        ).catch(()=> {
          this.informacionEmpleadoSeleccionado = null;
          this.mostrarInformacionEmpleado = true;
          this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_INFO_EMPLEADO);
        }) 
      : Promise.resolve();
    let promesaPruebasCentro = this.servicioAdministracionPruebasCentro.getPruebasQuePuedeTratarCentroDeAdmin().then(
      (listadoPruebas: any) => {
        this.listaPruebasDeCentro = listadoPruebas.map((idPrueba: number) => this.servicioAplicacion.listadoPruebas.find((prueba: any) => prueba.id === idPrueba));
        this.listaPruebasDisponibles = this.listaPruebasDeCentro;
      }
    ).catch(() => {
      this.listaPruebasDeCentro = [];
      this.listaPruebasDisponibles = [];
      this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_CENTRO);
    });
    if(this.modoEditarEmpleado) {
      Promise.all([promesaInfoEmpleado, promesaPruebasCentro]).then(
        () => {
          this.servicioAdministracionPlantilla.getPruebasQuePuedeTratarEmpleado(this.informacionEmpleadoSeleccionado.id).then(
            (listaPruebas: any) => {
              this.formulario.get("pruebasTratables").setValue(listaPruebas.map((nombrePrueba: String) => this.servicioAplicacion.listadoPruebas.find((prueba: any) => prueba.nombre === nombrePrueba)));
              this.actualizarListaPruebasDisponibles();
            }
          ).catch(() => {
            this.formulario.get("pruebasTratables").setValue([]);
            this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_EMPLEADO);
          })
        }
      ). catch(() => this.formulario.get("pruebasTratables").setValue([]))
    }
  }

  ngOnInit(): void {}

  buscarInformacionDeCandidato() {
    if(this.formulario.get("seguridadSocial").valid) {
      this.servicioAdministracionPlantilla.buscarInformacionCandidatoPorSeguridadSocial(this.formulario.get("seguridadSocial").value).then(
        (datosEmpleado: any) => {
          this.informacionEmpleadoSeleccionado = datosEmpleado;
          this.mostrarInformacionEmpleado = true;
        }
      ).catch((error)=> {
        this.listaPruebasDeCentro = [];
        this.listaPruebasDisponibles = [];
        this.informacionEmpleadoSeleccionado = null;
        this.mostrarInformacionEmpleado = true;
        this.formulario.get("pruebasTratables").setValue([]);
        if(error.codigo.contains("candidato_es_empleado")) {
          this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_CANDIDATO_ES_EMPLEADO);
        } else {
          this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_INFO_EMPLEADO);
        }
      });
    } else {
      this.informacionEmpleadoSeleccionado = null;
    }
  }

  actualizarListaPruebasDisponibles() {
    const listaPruebasSeleccionadas: any[] = this.formulario.get("pruebasTratables").value;
     this.listaPruebasDisponibles = this.listaPruebasDeCentro.filter(
      (prueba: any) => !listaPruebasSeleccionadas.some(
        (pruebaSeleccionada) => pruebaSeleccionada.id === prueba.id
      )
    ).sort((prueba1, prueba2) => prueba1.nombre.toLowerCase().localeCompare(prueba2.nombre.toLowerCase()));
  }

  annadirPruebaAEmpleado(prueba: any) {
    this.formulario.get("pruebasTratables").value.push(prueba)
    this.formulario.get("pruebasTratables").setValue(this.formulario.get("pruebasTratables").value.sort(
      (prueba1: any, prueba2: any) => prueba1.nombre.toLowerCase().localeCompare(prueba2.nombre.toLowerCase())
    ));
    this.actualizarListaPruebasDisponibles();
  }

  deseleccionarPruebaDeEmpleado(prueba: any) {
    this.formulario.get("pruebasTratables").value.splice(this.formulario.get("pruebasTratables").value.indexOf(prueba),1);
    this.formulario.get("pruebasTratables").updateValueAndValidity();
    this.listaPruebasDisponibles.push(prueba);
    this.actualizarListaPruebasDisponibles();
  }

  volverAAdministrarPlantilla() {
    this.enrutador.navigate(['administrar-plantilla-centro']);
  }

  guardarCandidatoEmpleado() {
    this.formulario.markAllAsTouched();
    if(this.formulario.valid) {
      let json = {
        seguridadSocial: this.formulario.get("seguridadSocial").getRawValue(),
        pruebasTratables: convertirListaAString(this.formulario.get("pruebasTratables").value.map((prueba: any) => prueba.id).sort()),
      }
      const servicio = this.modoEditarEmpleado
        ? this.servicioAdministracionPlantilla.actualizarEmpleado(json)
        : this.servicioAdministracionPlantilla.annadirCandidatoAPlantilla(json);
      servicio.then(
        () => {
          this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.EMPLEADO_ANNADIDO);
          this.volverAAdministrarPlantilla();
        }
      ).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, this.modoEditarEmpleado ? Errores.MENSAJES.ERROR_ACTUALIZAR_EMPLEADO : Errores.MENSAJES.ERROR_ANNADIR_EMPLEADO)); 
    } else {
      if(this.modoEditarEmpleado) {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_EMPLEADO_SIN_PRUEBAS);
      } else {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, !this.formulario.get("seguridadSocial").valid
                                                            ? Errores.MENSAJES.ERROR_SIN_CANDIDATO_VALIDO
                                                            : Errores.MENSAJES.ERROR_CANDIDATO_SIN_PRUEBAS);
      }
    }
  }

  

}
