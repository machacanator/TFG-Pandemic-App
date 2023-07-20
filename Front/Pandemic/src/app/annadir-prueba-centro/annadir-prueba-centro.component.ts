import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { validadorHoraInicioyFin, validadorMultiplesHorarios } from 'src/constantes/globales';
import { ServicioAdministrarPruebasCentro } from '../administrar-pruebas-centro/administrar-pruebas-centro.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';

@Component({
  selector: 'app-annadir-prueba-centro',
  templateUrl: './annadir-prueba-centro.component.html',
  styleUrls: ['./annadir-prueba-centro.component.scss']
})
export class AnnadirPruebaCentro implements OnInit {

  formulario:FormGroup;
  modoEditarHorariosPrueba: boolean;
  listaPruebasAgregables: any[] = [];
  deshabilitarBotones: boolean = false;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioAdministracionPruebasCentro: ServicioAdministrarPruebasCentro,
  ) {
    this.modoEditarHorariosPrueba = !!window.localStorage.getItem('idPruebaCentro');
    this.iniciarFormulario();
    this.iniciarListados();
  }

  private iniciarFormulario() {
    this.formulario = new FormGroup({
      idPrueba: new FormControl({
        value: this.modoEditarHorariosPrueba ? +window.localStorage.getItem('idPruebaCentro') : 0,
        disabled: this.modoEditarHorariosPrueba
      }, [Validators.required, Validators.min(1)]),
      horarios: new FormArray([
        this.nuevoHorarioVacio(),
      ], [validadorMultiplesHorarios()]),
    });
  }

  private nuevoHorarioVacio(): FormGroup {
    return new FormGroup({
      horaInicio: new FormControl({value: '', disabled: false}, [Validators.required]),
      horaFin: new FormControl({value: '', disabled: false}, [Validators.required]),
    }, [validadorHoraInicioyFin()]);
  }

  private nuevoHorarioRelleno(horaInicio: string, horaFin:string): FormGroup {
    return new FormGroup({
      horaInicio: new FormControl({value: this.convertirCadenaAFecha(horaInicio), disabled: false}, [Validators.required]),
      horaFin: new FormControl({value: this.convertirCadenaAFecha(horaFin), disabled: false}, [Validators.required]),
    }, []);
  }

  private iniciarListados() {
    this.servicioAdministracionPruebasCentro.getPruebasQuePuedeTratarCentroDeAdmin().then(
      (listadoPruebas: any) => {
        this.listaPruebasAgregables = this.servicioAplicacion.listadoPruebas.filter((prueba: any) => !listadoPruebas.includes(prueba.id));
        if(this.modoEditarHorariosPrueba && !this.listaPruebasAgregables.some((prueba: any) => prueba.id === +window.localStorage.getItem('idPruebaCentro'))) {
          const idPruebaSeleccionada = window.localStorage.getItem('idPruebaCentro');
          this.listaPruebasAgregables.push(this.servicioAplicacion.listadoPruebas.find((prueba: any) => prueba.id === +idPruebaSeleccionada));
        }
      }
    ).catch((error) => {
      this.listaPruebasAgregables = [];
      if(this.modoEditarHorariosPrueba) {
        const idPruebaSeleccionada = window.localStorage.getItem('idPruebaCentro');
        this.listaPruebasAgregables.push(this.servicioAplicacion.listadoPruebas.find((prueba: any) => prueba.id === +idPruebaSeleccionada))
      }
      this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_CENTRO);
    });

    if(this.modoEditarHorariosPrueba) {
      this.servicioAdministracionPruebasCentro.getHorariosPruebaCentroDeAdmin(window.localStorage.getItem('idPruebaCentro')).then(
        (infoHorarios: any) => {
          this.annadirListaDeHorariosAFormulario(infoHorarios);
        }
      ).catch(() => {
        this.formulario.get("horarios").setValue(new FormArray([
          this.nuevoHorarioVacio(),
        ], [validadorMultiplesHorarios()]));
        this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_HORARIOS_PRUEBA_CENTRO);
      })
    }
  }

  private convertirCadenaAFecha(fechaCadena:string): Date {
    let fechaHoy: Date = new Date();
    const arrayCadena = fechaCadena.split(":");
    fechaHoy.setHours(parseInt(arrayCadena[0]));
    fechaHoy.setMinutes(parseInt(arrayCadena[1]));
    fechaHoy.setSeconds(0);
    return fechaHoy;
  }

  private annadirListaDeHorariosAFormulario(listaDeHorarios: any []) {
    this.iniciarFormulario();
    let formularioHorarios: FormArray = this.formulario.get('horarios') as FormArray;
    listaDeHorarios.forEach((infoHorario: any, indice: number) => {
      if(indice === 0) {
        formularioHorarios.at(0).get('horaInicio').setValue(this.convertirCadenaAFecha(infoHorario.horaInicio));
        formularioHorarios.at(0).get('horaFin').setValue(this.convertirCadenaAFecha(infoHorario.horaFin));
      } else {
        formularioHorarios.push(this.nuevoHorarioRelleno(infoHorario.horaInicio, infoHorario.horaFin))
      }
    });
  }

  ngOnInit(): void {}

  annadirNuevoHorarioACentro() {
    let horarios = this.formulario.get("horarios") as FormArray;
    horarios.push(this.nuevoHorarioVacio());
  }

  borrarTodosLosHorarios() {
    this.deshabilitarBotones = true;
    let listadoDeHorarios = this.formulario.get('horarios') as FormArray;
    let HTMLTodosLosHorarios = Array.from(document.getElementsByClassName('horario__contenedor'));
    if(listadoDeHorarios.length === HTMLTodosLosHorarios.length) {
      let promesasBorradoDeHorarios: any[] = [];
      HTMLTodosLosHorarios.forEach((HTMLhorario: any, indiceHorario: number) => {
        setTimeout(()=>{
          HTMLhorario.classList?.add("horario__contenedor--ocultar");
          promesasBorradoDeHorarios.push(setTimeout(() => {
            listadoDeHorarios.removeAt(indiceHorario);
            if(indiceHorario === 0) {
              this.annadirNuevoHorarioACentro();
              this.deshabilitarBotones = false;
            }
          }, 1000));
        }, 500 * (HTMLTodosLosHorarios.length - indiceHorario));
      });
    } 
  }

  volverAAdministrarPruebasDeCentro() {
    this.enrutador.navigate(['administrar-pruebas-centro']);
  }

  eliminarHorario(indiceHorario : number, HTMLhorario: any) {
    if(indiceHorario > 0 && HTMLhorario) {
      let listadoDeHorarios = this.formulario.get('horarios') as FormArray;
      if(listadoDeHorarios.length === 2)this.deshabilitarBotones = true;
      HTMLhorario.classList?.add("horario__contenedor--ocultar");
      setTimeout(() => {
        listadoDeHorarios.removeAt(indiceHorario);
        this.deshabilitarBotones = false;
      }, 1000);
    }
  }

  getHorariosUnaSolaCadena(): string {
    let cadenaResultado: string = "";
    this.formulario.get('horarios').value.forEach((formularioHorario: any) => {
      cadenaResultado += this.getFechaFormatoHoraEstandar(formularioHorario.horaInicio)+"&"+this.getFechaFormatoHoraEstandar(formularioHorario.horaFin)+";"
    });
    return cadenaResultado;
  }

  getFechaFormatoHoraEstandar(fecha: Date) {
    return fecha.getHours()+":"+fecha.getMinutes()+":00";
  }

  getFechaFormatoHoraAMPM(fecha: Date) {
    let horas = fecha.getHours();
    const minutos = fecha.getMinutes();
    const ampm = horas >= 12 ? 'PM' : 'AM';
    horas = horas % 12;
    horas = horas ? horas : 12;
    const cadenaHoras = horas < 10 ? '0'+horas : horas;
    const cadenaMinutos = minutos < 10 ? '0'+minutos : minutos;
    return cadenaHoras + ':' + cadenaMinutos + ' ' + ampm;
  }

  annadirPruebaACentro() {
    this.formulario.markAllAsTouched();
    if(this.formulario.valid) {
      let json = {
        idPrueba: this.formulario.get('idPrueba').getRawValue(),
        horarios: this.getHorariosUnaSolaCadena(),
      }
      this.servicioAdministracionPruebasCentro.annadirNuevaPruebaACentro(json).then(
          () => {
              this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.PRUEBA_CENTRO_ANNADIDA);
              this.volverAAdministrarPruebasDeCentro();
            }
      ).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ANNADIR_PRUEBA_CENTRO)); 
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.ERROR_FORMULARIO);
    }
  }
  
  actualizarHorariosPrueba() {
    this.formulario.markAllAsTouched();
    if(this.formulario.valid) {
      let json = {
        idPrueba: this.formulario.get('idPrueba').getRawValue(),
        horarios: this.getHorariosUnaSolaCadena(),
      }
      this.servicioAdministracionPruebasCentro.actualizarHorariosPruebaCentro(json).then(
        () => {
          this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.HORARIOS_PRUEBA_CENTRO_ACTUALIZADOS);
          this.volverAAdministrarPruebasDeCentro();
        }
      ).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ACTUALIZAR_PRUEBA_CENTRO)); 
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.ERROR_FORMULARIO);
    }
  }

}
