import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { validadorMultiplesRangosEdad } from 'src/constantes/globales';
import { ServicioAdministrarPruebasComunidad } from '../administrar-carpetas-pruebas-comunidad/administrar-pruebas-comunidad.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';

@Component({
  selector: 'app-annadir-prueba-comunidad',
  templateUrl: './annadir-prueba-comunidad.component.html',
  styleUrls: ['./annadir-prueba-comunidad.component.scss']
})
export class AnnadirPruebaComunidad implements OnInit {

  formulario: FormGroup;
  modoEditarPrueba: boolean = false;
  anchuraVentana: number;
  deshabilitarAccionesConRangos = false;
  tiposDeRangos: any = Globales.TIPOS_RANGOS_EDAD;
  tiposDeSeparacionesEntrePruebas: any = Globales.TIPOS_SEPARACION_ENTRE_PRUEBAS;
  listadoTiposDeRangos: any[];
  listadoTiposDeSeparacion: any[];
  listadoDeRangos: FormGroup[];
  indiceRangoErroneo: number = null;


  constructor(
    private enrutador: Router,
    private desinfectante: DomSanitizer,
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioAdministrarPruebasComunidad: ServicioAdministrarPruebasComunidad,
  ) {
    this.modoEditarPrueba = !!window.localStorage.getItem('idPrueba');
    this.iniciarFormulario();
    this.iniciarListadoTiposDeRangosDeEdad();
    this.iniciarListadoTiposDeSeparacionDePruebas();
    this.tiposDeRangos
    if(this.modoEditarPrueba) {
      this.cargarDatosPrueba(Number.parseInt(window.localStorage.getItem('idPrueba')));
    }
  }

  private iniciarListadoTiposDeRangosDeEdad() {
    this.listadoTiposDeRangos = Object.values(Globales.TIPOS_RANGOS_EDAD).map((valor: number) => {
      let nombre = '';
      switch (valor) {
        case Globales.TIPOS_RANGOS_EDAD.MENOR_QUE: nombre = 'Menor que'; break;
        case Globales.TIPOS_RANGOS_EDAD.ENTRE: nombre = 'Entre'; break;
        case Globales.TIPOS_RANGOS_EDAD.MAYOR_QUE: nombre = 'Mayor que'; break;
        case Globales.TIPOS_RANGOS_EDAD.TODOS: nombre = 'Todos'; break;
        default: nombre = '';
      }
      return {
        nombre,
        valor,
      };
    });
  }

  private iniciarListadoTiposDeSeparacionDePruebas() {
    this.listadoTiposDeSeparacion = Object.values(Globales.TIPOS_SEPARACION_ENTRE_PRUEBAS).map((valor: number) => {
      let nombre = '';
      switch (valor) {
        case Globales.TIPOS_SEPARACION_ENTRE_PRUEBAS.DIAS: nombre = 'Dias'; break;
        case Globales.TIPOS_SEPARACION_ENTRE_PRUEBAS.SEMANAS: nombre = 'Semanas'; break;
        case Globales.TIPOS_SEPARACION_ENTRE_PRUEBAS.MESES: nombre = 'Meses'; break;
        case Globales.TIPOS_SEPARACION_ENTRE_PRUEBAS.ANNOS: nombre = 'Años'; break;
        default: nombre = '';
      }
      return {
        nombre,
        valor,
      };
    });
  }

  private iniciarFormulario() {
    this.formulario = new FormGroup({
      nombre: new FormControl({value: '', disabled: false}, [Validators.required]),
      duracion: new FormControl({value: '', disabled: false}, [Validators.required, Validators.pattern('^[0-9]+$')]),
      descripcion: new FormControl({value: '', disabled: false}, [Validators.required]),
      separacionCantidad: new FormControl({value: '', disabled: false}, [Validators.required, Validators.pattern('^[0-9]+$')]),
      separacionTipo: new FormControl({value: '', disabled: false}, [Validators.required]),
      avisos: new FormControl({value: '', disabled: false}, []),
      recomendaciones: new FormControl({value: '', disabled: false}, []),
    });
    this.listadoDeRangos = [
      new FormGroup({
        tipo: new FormControl({value:'', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.min(1)]),
        muyRecomendable: new FormControl({value: false, disabled: this.deshabilitarAccionesConRangos}, [Validators.required])
      })
    ];
  }

  private cargarDatosPrueba (idPrueba: number) {
    const infoPrueba = this.servicioAplicacion.listadoPruebas.find(prueba => prueba.id === idPrueba);
    this.formulario.get("nombre").setValue(infoPrueba.nombre);
    this.formulario.get("duracion").setValue(infoPrueba.duracionPrueba);
    this.cargarDatosSeparacion(infoPrueba);
    this.formulario.get("descripcion").setValue(infoPrueba.descripcion);
    this.formulario.get("avisos").setValue(infoPrueba.avisos);
    this.formulario.get("recomendaciones").setValue(infoPrueba.recomendaciones);
    this.servicioAdministrarPruebasComunidad.getRangosDeEdadDePrueba(idPrueba)
      .then(
        (listaRangosDeEdad: any) => {
          this.listadoDeRangos = this.getRangosFormatoFormularioFront(listaRangosDeEdad);
          this.ordenarRangos();
        }
      ).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_RANGOS_EDAD_PRUEBA)); 
  }

  private cargarDatosSeparacion(datosPrueba: any) {
    const separacionEnDias = parseInt(datosPrueba.separacionSiguientePrueba);
    if(separacionEnDias % 365 === 0) {
      this.formulario.get("separacionTipo").setValue(this.tiposDeSeparacionesEntrePruebas.ANNOS);
      this.formulario.get("separacionCantidad").setValue((separacionEnDias/365).toString());
    } else if(separacionEnDias % 30 === 0) {
      this.formulario.get("separacionTipo").setValue(this.tiposDeSeparacionesEntrePruebas.MESES);
      this.formulario.get("separacionCantidad").setValue((separacionEnDias/30).toString());
    } else if(separacionEnDias % 7 === 0) {
      this.formulario.get("separacionTipo").setValue(this.tiposDeSeparacionesEntrePruebas.SEMANAS);
      this.formulario.get("separacionCantidad").setValue((separacionEnDias/7).toString());
    } else {
      this.formulario.get("separacionTipo").setValue(this.tiposDeSeparacionesEntrePruebas.DIAS);
      this.formulario.get("separacionCantidad").setValue((separacionEnDias).toString());
    }
  }

  private getRangosFormatoFormularioFront(listadoRangosBack: any[]) {
    return listadoRangosBack.map((rango: any) => {
      let tipoDeRango = null;
      if(rango.edadInicio && rango.edadFin) {
        tipoDeRango = this.tiposDeRangos.ENTRE;
      } else if (!rango.edadInicio && !rango.edadFin) {
        tipoDeRango = this.tiposDeRangos.TODOS;
      } else {
        tipoDeRango = rango.edadInicio ? this.tiposDeRangos.MAYOR_QUE : this.tiposDeRangos.MENOR_QUE;
      }
      let formulario: FormGroup = new FormGroup({
        tipo: new FormControl({value: tipoDeRango, disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.min(1)]),
        muyRecomendable: new FormControl({value: rango.muyRecomendable, disabled: this.deshabilitarAccionesConRangos}, [Validators.required]),    
      });
      switch(tipoDeRango) {
        case this.tiposDeRangos.MAYOR_QUE:
          formulario.addControl('edad', new FormControl({value: rango.edadInicio ? rango.edadInicio : '', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.pattern('^[0-9]+$')]));
          break;  
        case this.tiposDeRangos.MENOR_QUE: 
          formulario.addControl('edad', new FormControl({value: rango.edadFin ? rango.edadFin : '', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.pattern('^[0-9]+$')]));
          break;
        case this.tiposDeRangos.ENTRE:
          formulario.addControl('edadInicio', new FormControl({value: rango.edadInicio ? rango.edadInicio : '', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.pattern('^[0-9]+$')]));
          formulario.addControl('edadFin', new FormControl({value: rango.edadFin ? rango.edadFin : '', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.pattern('^[0-9]+$')]));
          break;
        case this.tiposDeRangos.TODOS:
          this.listadoDeRangos = [formulario];
          break; 
      }
      this.validarRangosDeEdad();
      return formulario;
    });
  }

  private getRangosFormatoBack(): any[] {
    
    if(this.listadoDeRangos) {
      return this.listadoDeRangos.map((formularioRango: FormGroup) => {
        const rango = formularioRango.value;
        switch(rango.tipo) {
          case this.tiposDeRangos.MAYOR_QUE:
            return {
              muyRecomendable: rango.muyRecomendable,
              edadInicio: rango.edad, 
              edadFin: null,
            };
          case this.tiposDeRangos.MENOR_QUE:
            return {
              muyRecomendable: rango.muyRecomendable,
              edadInicio: null, 
              edadFin: rango.edad,
            };
          case this.tiposDeRangos.ENTRE:
            return {
              muyRecomendable: rango.muyRecomendable,
              edadInicio: rango.edadInicio, 
              edadFin: rango.edadFin,
            };
          case this.tiposDeRangos.TODOS:
            return {
              muyRecomendable: rango.muyRecomendable,
              edadInicio: null, 
              edadFin: null,
            };
        }
        return null;
      });
    }
    return [];
  }

  private getSeparacionEnDias(): number {
    let multiplicador = 1;

    switch(this.formulario.get("separacionTipo").value) {
      case this.tiposDeSeparacionesEntrePruebas.DIAS: multiplicador = 1; break;
      case this.tiposDeSeparacionesEntrePruebas.SEMANAS: multiplicador = 7; break;
      case this.tiposDeSeparacionesEntrePruebas.MESES: multiplicador = 30; break;
      case this.tiposDeSeparacionesEntrePruebas.ANNOS: multiplicador = 365; break;
    }

    return parseInt(this.formulario.get("separacionCantidad").value) * multiplicador;
  }

  ngOnInit(): void {
    this.cambioAnchuraVentana();
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }

  volverAAdministrarPruebasComunidad() {
    this.enrutador.navigate(['administrar-pruebas-comunidad/pruebas']);
  }

  getControlador(nombreControlador: string): FormControl {
    return this.formulario.controls[nombreControlador] as FormControl;
  }

  validarRangosDeEdad() {
    this.indiceRangoErroneo =  validadorMultiplesRangosEdad(this.listadoDeRangos)?.forbiddenRange?.value;
  }

  esListadoRangosValido(): boolean {
    return this.listadoDeRangos && this.listadoDeRangos.length > 0 && !this.indiceRangoErroneo
  }

  cambioTipoDeRango(nuevoTipoDeRango: any, formulario: FormGroup) {
    Object.keys(formulario.controls).forEach(nombreCampo => {
      if(nombreCampo !== 'tipo' && nombreCampo !== 'muyRecomendable') formulario.removeControl(nombreCampo);
    })
    switch(nuevoTipoDeRango) {
      case this.tiposDeRangos.MAYOR_QUE:
        case this.tiposDeRangos.MENOR_QUE: 
        formulario.addControl('edad', new FormControl({value: '', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.pattern('^[0-9]+$')]));
        break;
      case this.tiposDeRangos.ENTRE:
        formulario.addControl('edadInicio', new FormControl({value: '', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.pattern('^[0-9]+$')]));
        formulario.addControl('edadFin', new FormControl({value: '', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.pattern('^[0-9]+$')]));
        break;
      case this.tiposDeRangos.TODOS:
        this.listadoDeRangos = [formulario];
        break; 
    }
    this.validarRangosDeEdad();
  }
        
  annadirNuevoRango() {
    this.listadoDeRangos.push(new FormGroup({
      tipo: new FormControl({value:'', disabled: this.deshabilitarAccionesConRangos}, [Validators.required, Validators.min(1)]),
      muyRecomendable: new FormControl({value: false, disabled: this.deshabilitarAccionesConRangos}, [Validators.required]),
    }));
  }

  quitarRangoDeListado(indice: any) {
    this.listadoDeRangos.splice(indice, 1);
    if(indice === this.indiceRangoErroneo || indice === this.indiceRangoErroneo - 1) this.validarRangosDeEdad();
  }

  ordenarRangos(){
    this.listadoDeRangos = this.listadoDeRangos.sort((formularioRango1: FormGroup, formularioRango2: FormGroup) =>  {
      const rango1 = formularioRango1.value;
      const rango2 = formularioRango2.value;
      if(rango1.tipo !== rango2.tipo) return rango1.tipo - rango2.tipo;
      switch(rango1.tipo) {
        case Globales.TIPOS_RANGOS_EDAD.MENOR_QUE:
        case Globales.TIPOS_RANGOS_EDAD.MAYOR_QUE:  return rango1.edad - rango2.edad;
        case Globales.TIPOS_RANGOS_EDAD.ENTRE:  return rango1.edadInicio - rango2.edadInicio;
        case Globales.TIPOS_RANGOS_EDAD.TODOS: return 1;
        default: return 0;
      }
    });
  }

  guardarPrueba() {
    this.validarRangosDeEdad();
    this.formulario.markAllAsTouched();
    if(this.formulario.valid && this.esListadoRangosValido()) {
      const json = {
        ...this.formulario.value,
        separacionSiguientePrueba: this.getSeparacionEnDias(),
        rangosEdad: this.getRangosFormatoBack(),
      }
      const servicio = this.modoEditarPrueba
        ? this.servicioAdministrarPruebasComunidad.actualizarPrueba({id: window.localStorage.getItem('idPrueba'), ...json})
        : this.servicioAdministrarPruebasComunidad.annadirNuevaPrueba(window.localStorage.getItem('idCarpetaPruebas'), json);
      servicio.then(
        () => {
          this.servicioAplicacion.getPruebasComplementarias();
          this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, this.modoEditarPrueba ? Globales.MENSAJES.PRUEBA_ACTUALIZADA : Globales.MENSAJES.PRUEBA_ANNADIDA);      
          this.volverAAdministrarPruebasComunidad();
        }
      ).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, this.modoEditarPrueba ? Errores.MENSAJES.ERROR_ACTUALIZAR_PRUEBA : Errores.MENSAJES.ERROR_ANNADIR_PRUEBA)); 
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO);
    }
  }

}
