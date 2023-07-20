import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormArray, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { Router } from '@angular/router';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import Globales, { validadorFecha } from 'src/constantes/globales';
import { CabeceraCalendarioCitas } from '../cabecera-calendario-citas/cabecera-calendario-citas.component';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioNuevaCita } from './servicio-nueva-cita.service';

@Component({
  selector: 'app-nueva-cita',
  templateUrl: './nueva-cita.component.html',
  styleUrls: ['./nueva-cita.component.scss'],
})
export class NuevaCita implements OnInit {
  @ViewChild('graficoPasos') private graficoPasos: MatStepper;
  cabeceraCalendario = CabeceraCalendarioCitas;

  modoEditarCita: boolean = false;
  datosCitaSeleccionada: any;
  formularios: FormArray<FormGroup>;
  listadoCentros: any[] = [];
  listadoPruebasDisponibles: any[] = [];
  listadoDiasDisponibles: string[] = [];
  listadoHorasDisponibles: any[] = [];
  numeroDeFilasDeHoras: number[];
  horasPorFila: number = 4;
  fechaHoy: string = moment(new Date()).format(Globales.FORMATO_FECHA_NACIONAL).replaceAll("/", "-");
  animacionCompletada = true;
  duracionAnimacionGrafico = 1*Globales.SEGUNDOS;
  filtroFechas = this.getFuncionFiltroFechas();

  constructor(
    private enrutador: Router,
    private servicioCitas: ServicioNuevaCita,
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
  ) {
    this.modoEditarCita = !!window.localStorage.getItem('idCita');
    this.iniciarFormularios();
    this.iniciarListados();
    this.iniciarPasos();
  }

  private iniciarListados() {
    this.servicioCitas.cambioCabeceraCalendario.subscribe((fecha: moment.Moment) => {
      this.servicioCitas.getDiasDisponiblesCentro(this.formularios.at(0).get("prueba").value, this.formularios.at(1).get("centro").value, fecha.year(), fecha.month()+1)
        .then((listaDias: any) => {this.listadoDiasDisponibles = listaDias; this.filtroFechas = this.getFuncionFiltroFechas();})
        .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_DIAS_DISPONIBLES);});
    });
    this.servicioCitas.getPruebasDisponiblesUsuario()
    .then((listaPruebas: any) => {
      this.listadoPruebasDisponibles = listaPruebas;
      if(this.modoEditarCita) {
        this.servicioCitas.getInfoCita(Number(window.localStorage.getItem('idCita')))
        .then(
          (datosCita: any) => {
            this.datosCitaSeleccionada = datosCita;
            this.servicioCitas.getInfoPrueba(this.datosCitaSeleccionada.prueba)
              .then((infoPrueba: any) => {
                this.listadoPruebasDisponibles.push(infoPrueba);
                this.formularios.at(0).get('prueba').setValue(this.datosCitaSeleccionada.prueba);
                this.cambioPrueba(this.datosCitaSeleccionada.prueba);
                this.formularios.at(1).get('centro').setValue(this.datosCitaSeleccionada.centro);
                this.cambioCentro(this.datosCitaSeleccionada.centro);
                let fecha: moment.Moment= moment(this.datosCitaSeleccionada.fecha);
                this.servicioCitas.getDiasDisponiblesCentro(this.formularios.at(0).get("prueba").value, this.datosCitaSeleccionada.centro, fecha.toDate().getFullYear(), fecha.toDate().getMonth() + 1)
                  .then((listaDias: any) => {
                    this.listadoDiasDisponibles = listaDias;
                    this.filtroFechas = this.getFuncionFiltroFechas();
                    this.formularios.at(2).get("fecha").setValue(fecha);
                    this.cambioFecha();
                  })
                  .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_DIAS_DISPONIBLES);});
              })
              .catch(()=>{this.volverACitasPendientes(); this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_INFO_PRUEBA)});
          }
        )
        .catch(()=>{this.volverACitasPendientes(); this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_SOLICITAR_CITA)});          
      }
    })
    .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_PRUEBAS_RECOMENDADAS_PACIENTE);});
  }

  private iniciarPasos() {
    document.getElementById('paso' + 2)?.classList.add("paso--ocultado");
    document.getElementById('paso' + 3)?.classList.add("paso--ocultado");
    document.getElementById('paso' + 4)?.classList.add("paso--ocultado");
    this.seleccionarPaso(1);
  }

  private getFuncionFiltroFechas() {
    return (fecha: moment.Moment) => this.listadoDiasDisponibles?.includes(
      fecha?.format(Globales.FORMATO_FECHA_NACIONAL).replaceAll("/", "-"));
  }
  
  private iniciarFormularios() {
    this.formularios = new FormArray<FormGroup>([
      new FormGroup({
        prueba: new FormControl({value: 0, disabled: this.modoEditarCita}, [Validators.required, Validators.min(1)]),
      }),
      new FormGroup({
        centro: new FormControl({value: 0, disabled: false}, [Validators.required, Validators.min(1)]),
      }),
      new FormGroup({
        fecha: new FormControl({value: null, disabled: false},[Validators.required, validadorFecha()]),
        hora: new FormControl({value: '', disabled: false},[Validators.required]),
      }),
    ]);
    // this.formularios.markAllAsTouched();
  }
  
  private reiniciarPagina() {
    this.servicioCitas.getPruebasDisponiblesUsuario()
      .then((listaPruebas: any) => {this.listadoPruebasDisponibles = listaPruebas})
      .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_PRUEBAS_RECOMENDADAS_PACIENTE);});
    this.iniciarFormularios();
    this.listadoCentros = [];
    this.listadoPruebasDisponibles = [];
    this.listadoDiasDisponibles = [];
    this.listadoHorasDisponibles = [];
    this.numeroDeFilasDeHoras = [];
    this.graficoPasos?.reset();
    this.iniciarPasos();
  }

  
  ngOnInit(): void {
    this.iniciarPasos();
    this.cambioAnchuraVentana();
  }
  
  getNombrePaciente() {
    return this.servicioAplicacion.nombreCompletoUsuario;
  }
  
  getNombrePrueba() {
    let idPrueba = this.formularios.at(0).get("prueba").value;
    return this.listadoPruebasDisponibles?.find(prueba => prueba.id === idPrueba)?.nombre;
  }
  
  getNombreCentro() {
    let idCentro = this.formularios.at(1).get("centro").value;
    return this.listadoCentros?.find(centro => centro.id === idCentro)?.nombre;
  }
  
  getFechaSeleccionada() {
    return this.formularios.at(2).get("fecha").value?.format(Globales.FORMATO_FECHA_NACIONAL).replaceAll("/", "-");
  }

  esFechaSeleccionadaHoy(): boolean {
    return this.getFechaSeleccionada() === this.fechaHoy;
  }
  
  getEspacioEnBlanco() {
    return new Array(this.horasPorFila - this.listadoHorasDisponibles.length%this.horasPorFila);
  }

  getFilasDeHorasHoy() {
    const listaHoras = this.getListadoHorasDisponiblesHoy();
    const filas: number = listaHoras.length % this.horasPorFila === 0 ? listaHoras.length/this.horasPorFila : (listaHoras.length/this.horasPorFila) + 1;
    return Array.from(Array(Math.trunc(filas)).keys());
  }

  getListadoHorasDisponiblesHoy() {
    return this.listadoHorasDisponibles.filter((hora) => parseInt(hora.substring(0,2)) > moment().hours()
    || (parseInt(hora.substring(0,2)) === moment().hours() && (parseInt(hora.substring(3,5)) > moment().minutes())));
  }
  
  volverACitasPendientes() {
    this.enrutador.navigate(['citas-pendientes']);
  }

  seleccionarPaso(pasoASeleccionar: number) {
    this.animacionCompletada = false;
    if(this.graficoPasos) {
      document.getElementById('graficoPasos').classList.add('deshabilitado');
      const pasoAnteriormenteSeleccionado: number = this.graficoPasos.selectedIndex + 1;
      this.graficoPasos.selectedIndex = pasoASeleccionar - 1;
      this.deseleccionarPaso(pasoAnteriormenteSeleccionado);
    }
    setTimeout(() => {
      document.getElementById('paso' + pasoASeleccionar)?.classList.remove("paso--ocultado");
      document.getElementById('paso' + pasoASeleccionar)?.classList.add("paso--seleccionado");
      document.getElementById('graficoPasos')?.classList.remove('deshabilitado');
      this.animacionCompletada = true;
    }, 1*Globales.SEGUNDOS);
  }
  
  deseleccionarPaso(numeroPasoADeseleccionar: number) {
    if(numeroPasoADeseleccionar) {
      document.getElementById('paso' + numeroPasoADeseleccionar)?.classList.remove("paso--seleccionado");
      document.getElementById('paso' + numeroPasoADeseleccionar)?.classList.add("paso--ocultar");
      setTimeout(()=>{
        document.getElementById('paso' + numeroPasoADeseleccionar)?.classList.remove("paso--ocultar");
        document.getElementById('paso' + numeroPasoADeseleccionar)?.classList.add("paso--ocultado");
      }, 1*Globales.SEGUNDOS)
    }
  }

  clickGraficoPaso(evento: any) {
    if((evento.selectedIndex < evento.previouslySelectedIndex
      || this.formularios.at(evento.previouslySelectedIndex)?.valid) && this.animacionCompletada) {
      this.seleccionarPaso(evento.selectedIndex + 1);
    }
  }

  cambioPrueba(idPrueba: number) {
    this.iniciarFormularios();
    this.formularios.at(0).get("prueba").setValue(idPrueba);
    if(idPrueba > 0) {
      this.servicioCitas.getCentrosConPruebaDisponible(idPrueba)
        .then((listaCentros: any) => {this.listadoCentros = listaCentros;})
        .catch(() => {this.listadoCentros = []; this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_CENTROS);});
    }
  }

  cambioCentro(idCentro: number) {
    this.listadoDiasDisponibles = [];
    this.formularios.at(2).get('fecha').setValue(null);
    this.formularios.at(2).get('hora').setValue('');
    if(idCentro > 0) {
      let fechaActual: Date = new Date();
      this.servicioCitas.getDiasDisponiblesCentro(this.formularios.at(0).get("prueba").value, idCentro, fechaActual.getFullYear(), fechaActual.getMonth() + 1)
        .then((listaDias: any) => {this.listadoDiasDisponibles = listaDias; this.filtroFechas = this.getFuncionFiltroFechas();})
        .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_DIAS_DISPONIBLES);});
    }
  }

  cambioFecha() {
    this.formularios.at(2).get("hora").setValue('');
    if(this.formularios.at(2).get("fecha").valid) {
      // let nuevaFecha: string = moment(this.formularios.at(2).get("fecha").value.toDate()).format(Globales.FORMATO_FECHA_NACIONAL);
      let nuevaFecha: string = this.formularios.at(2).get("fecha").value.format(Globales.FORMATO_FECHA_NACIONAL);
      this.servicioCitas.getHorasDisponiblesCentro(this.formularios.at(0).get("prueba").value, this.formularios.at(1).get("centro").value, nuevaFecha)
          .then((listaHoras: any) => {
            this.listadoHorasDisponibles = listaHoras;
            console.log(this.graficoPasos.selectedIndex)
            if (this.modoEditarCita && this.graficoPasos.selectedIndex === 0) {
              let fechaSolicitada: moment.Moment = this.formularios.at(2).get("fecha").value;
              let fechaCitaAEditar: moment.Moment = moment(this.datosCitaSeleccionada.fecha);
              if (fechaSolicitada.format(Globales.FORMATO_FECHA_NACIONAL) === fechaCitaAEditar.format(Globales.FORMATO_FECHA_NACIONAL)) {
                let horaCitaAEditar = fechaCitaAEditar.format(Globales.FORMATO_HORA).substring(0,5);
                this.listadoHorasDisponibles.push(horaCitaAEditar);
                this.listadoHorasDisponibles.sort();
                this.formularios.at(2).get("hora").setValue(horaCitaAEditar);
                this.seleccionarPaso(3);
              }
            }
            let filas: number = listaHoras.length % this.horasPorFila === 0 ? listaHoras.length/this.horasPorFila : (listaHoras.length/this.horasPorFila) + 1;
            this.numeroDeFilasDeHoras = Array.from(Array(Math.trunc(filas)).keys());
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_HORAS_DISPONIBLES);});
    } else {
      this.numeroDeFilasDeHoras = [];
      this.listadoHorasDisponibles = [];
    }
  }

  seleccionarHora(horaSeleccionada: string) {
    this.formularios.at(2).get("hora").setValue(horaSeleccionada);
  }

  cambioAnchuraVentana() {
    if(document.body.clientWidth > 600) {
      this.horasPorFila = 4;
    } else if (document.body.clientWidth <= 600 && document.body.clientWidth > 500) {
      this.horasPorFila = 3;
    } else {
      this.horasPorFila = 2;
    }
    
    let filas: number = this.listadoHorasDisponibles.length % this.horasPorFila === 0
      ? this.listadoHorasDisponibles.length/this.horasPorFila
      : (this.listadoHorasDisponibles.length/this.horasPorFila) + 1;
    this.numeroDeFilasDeHoras = Array.from(Array(Math.trunc(filas)).keys());
  }

  haHechoAlgunCambioEnFormulario() {
    let fechaCitaAEditar = moment(this.datosCitaSeleccionada.fecha);
    return this.formularios.at(0).get('prueba').value != this.datosCitaSeleccionada.prueba 
      || this.formularios.at(1).get('centro').value != this.datosCitaSeleccionada.centro
      || this.formularios.at(2).get('fecha').value.format(Globales.FORMATO_FECHA_NACIONAL) != fechaCitaAEditar.format(Globales.FORMATO_FECHA_NACIONAL)
      || this.formularios.at(2).get('hora').value != fechaCitaAEditar.format(Globales.FORMATO_HORA).substring(0,5);
  }

  pedirCita() {
    this.formularios.markAllAsTouched();
    if(this.formularios.valid) {
      let json: any = {
        prueba: this.formularios.at(0).get("prueba").getRawValue(),
        centro: this.formularios.at(1).get("centro").value,
        fecha: this.getFechaSeleccionada()+" "+this.formularios.at(2).get("hora").value+":00",
      }
      if (this.modoEditarCita) {
        json.id = this.datosCitaSeleccionada.id;
        this.servicioCitas.modificarCita(json)
        .then(() => {
          this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.CITA_CAMBIADA);
          this.volverACitasPendientes();
        })
        .catch(() => {this.volverACitasPendientes(); this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_SOLICITAR_CITA);});
      } else {
        this.servicioCitas.pedirCita(json)
          .then(() => {
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.CITA_SOLICITADA);
            this.seleccionarPaso(1);
            setTimeout(()=>{
              this.formularios.markAsUntouched();
              this.reiniciarPagina();
            }, 1*Globales.SEGUNDOS);
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_SOLICITAR_CITA);});
        this.formularios.markAsUntouched();
        this.reiniciarPagina();
      }
    } else {
      this.seleccionarPaso(this.formularios.controls.findIndex(paso => !paso.valid) + 1);
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO);
    }
  }
}
