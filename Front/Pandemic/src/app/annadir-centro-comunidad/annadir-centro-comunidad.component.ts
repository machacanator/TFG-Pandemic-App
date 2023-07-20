import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { formatearDatosImagenAArchivo } from 'src/constantes/globales';
import { ServicioAdministrarCentrosComunidad } from '../administrar-centros-comunidad/administrar-centros-comunidad.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';

export interface Imagen {
  archivo: File;
  url: SafeUrl;
}

@Component({
  selector: 'app-annadir-centro',
  templateUrl: './annadir-centro-comunidad.component.html',
  styleUrls: ['./annadir-centro-comunidad.component.scss']
})
export class AnnadirCentro implements OnInit {
  formulario: FormGroup;
  numeroSeguridadSocial: FormControl;
  modoEditarCentro: boolean = false;
  listaMunicipios: string[] = Globales.LISTADO_MUNICIPIOS;
  listaImagenesDeCentro: any[] = [];
  anchuraVentana: number;
  maximoNumeroDeImagenes = Globales.MAXIMO_IMAGENES_CENTRO;
  deshabilitarAccionesConImagenes = false;


  constructor(
    private enrutador: Router,
    private desinfectante: DomSanitizer,
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioAdministrarCentrosComunidad: ServicioAdministrarCentrosComunidad,
  ) {
    this.modoEditarCentro = !!window.localStorage.getItem('idCentro');
    this.iniciarFormulario();
    if(this.modoEditarCentro) {
      this.cargarDatosCentro(Number.parseInt(window.localStorage.getItem('idCentro')));
      this.iniciarListadoAdministradores();
    }
  }

  private iniciarFormulario() {
    this.numeroSeguridadSocial = new FormControl({value: '', disabled: false}, [Validators.minLength(9), Validators.pattern('^[0-9]{9}$')]);
    this.formulario = new FormGroup({
      nombre: new FormControl({value: '', disabled: false}, [Validators.required]),
      direccion: new FormControl({value: '', disabled: false}, [Validators.required]),
      municipio: new FormControl({value: '', disabled: false}, [Validators.required]),
      cartaPresentacion: new FormControl({value: '', disabled: false}, []),
      historia: new FormControl({value: '', disabled: false}, []),
      mision: new FormControl({value: '', disabled: false}, []),
      codigoPostal: new FormControl({value: '', disabled: false}, [Validators.required, Validators.pattern('^[0-9]{5}$')]),
      listadoAdministradores: new FormControl({value: [], disabled: false}, [Validators.required]),
    });
    
  }

  private cargarDatosCentro (idCentro: number) {
    const infoCentro = this.servicioAplicacion.listadoCentros.find(centro => centro.id === idCentro);
    this.formulario.get("nombre").setValue(infoCentro.nombre);
    this.formulario.get("direccion").setValue(infoCentro.direccion);
    this.formulario.get("municipio").setValue(infoCentro.municipio);
    this.formulario.get("cartaPresentacion").setValue(infoCentro.cartaPresentacion);
    this.formulario.get("historia").setValue(infoCentro.historia);
    this.formulario.get("mision").setValue(infoCentro.mision);
    this.formulario.get("codigoPostal").setValue(infoCentro.codigoPostal);
    this.servicioAdministrarCentrosComunidad.getImagenesDeCentro(idCentro).then(
      (imagenesCentro: any) => {
        this.listaImagenesDeCentro = imagenesCentro.map((imagen: any) => {
          const archivo = formatearDatosImagenAArchivo(imagen);
          return {
            archivo,
            url: this.desinfectante.bypassSecurityTrustUrl(window.URL.createObjectURL(archivo)),
          };
        });
      }
    ).catch(() => {this.listaImagenesDeCentro = []; this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_SOLICITAR_IMAGENES_CENTRO)});
  }

  private iniciarListadoAdministradores() {
    this.servicioAdministrarCentrosComunidad.getAdministradoresCentro(window.localStorage.getItem('idCentro')).then(
      (listaAdministradores: any) => {
        this.formulario.get("listadoAdministradores").setValue(listaAdministradores);
        this.formulario.get("listadoAdministradores").updateValueAndValidity();
      }
    ).catch( () => { this.formulario.get("listadoAdministradores").setValue([]); this.formulario.get("listadoAdministradores").updateValueAndValidity(); this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_ADMINISTRADORES_CENTRO);})
  }

  private estaImagenRepetida(nuevaImagen: File) {
    nuevaImagen.name
    return this.listaImagenesDeCentro.some((imagenYaAñadida: any) =>
      imagenYaAñadida.archivo.name === nuevaImagen.name && imagenYaAñadida.archivo.type === nuevaImagen.type);
  }

  ngOnInit(): void {
    this.cambioAnchuraVentana();
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }

  volverAAdministrarCentros() {
    this.enrutador.navigate(['administrar-centros-comunidad']);
  }

  getControlador(nombreControlador: string): FormControl {
    return this.formulario.controls[nombreControlador] as FormControl;
  }

  annadirImagen(evento: any) {
    const nuevaImagen: File = evento.target.files[0];
    if(nuevaImagen.size > Globales.MAXIMO_TAMAÑO_IMAGEN) {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.ERROR_IMAGEN_DEMASIADO_GRANDE);
    } else if(this.estaImagenRepetida(nuevaImagen)) {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.ERROR_IMAGEN_YA_SELECCIONADA);
    } else if(this.listaImagenesDeCentro.length >= this.maximoNumeroDeImagenes) {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.ERROR_MAXIMO_IMAGENES);
    } else {
      this.listaImagenesDeCentro.push({
        archivo: nuevaImagen,
        url: this.desinfectante.bypassSecurityTrustUrl(window.URL.createObjectURL(nuevaImagen)),
      });
    }
    //Necesario para poder seleccionar añadir la misma imagen las veces que sean necesarias
    evento.target.value = null;
  }

  eliminarImagen(indiceImagen: number, HTMLImagen: any) {
    this.deshabilitarAccionesConImagenes = true;
    HTMLImagen.classList.add("imagen--desaparecer")
    setTimeout(() => {
      if(this.listaImagenesDeCentro?.[indiceImagen]) {
        this.listaImagenesDeCentro.splice(indiceImagen, 1);
        this.deshabilitarAccionesConImagenes = false;
      }
    }, 1000);
  }

  annadirNuevoAdministrador() {
    if(this.numeroSeguridadSocial.valid) {
      this.numeroSeguridadSocial.markAllAsTouched();
      this.servicioAdministrarCentrosComunidad.getInfoAdministrador(this.numeroSeguridadSocial.value).then(
        (infoNuevoAdministrador: any) => {
          if(!this.formulario.get("listadoAdministradores").value.some((admin: any) => admin.id === infoNuevoAdministrador.id)) {
            this.formulario.get("listadoAdministradores").value.push(infoNuevoAdministrador);
            this.formulario.get("listadoAdministradores").updateValueAndValidity();
          }
        }
      ).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_INFO_ADMINISTRADOR));
    }
    this.numeroSeguridadSocial.setValue('');
  }

  quitarAdministradorDeListado(administrador: any) {
    this.formulario.get("listadoAdministradores").value.splice(this.formulario.get("listadoAdministradores").value.indexOf(administrador),1);
    this.formulario.get("listadoAdministradores").updateValueAndValidity();
  }

  guardarCentro() {
    this.formulario.markAllAsTouched();
    if(this.formulario.valid) {
      const json = {
        ...this.formulario.value,
        listadoAdministradores: this.formulario.get("listadoAdministradores").value.reduce((cadenaFinal: string, nuevoAdmin: any, posicionArray: number) => posicionArray > 0 ? cadenaFinal+","+nuevoAdmin.id.toString() : nuevoAdmin.id.toString(), ""),
      }
      const servicio = this.modoEditarCentro
        ? this.servicioAdministrarCentrosComunidad.actualizarCentro(window.localStorage.getItem('idCentro'), json)
        : this.servicioAdministrarCentrosComunidad.annadirNuevoCentro(json);
      servicio.then(
        () => {
          this.servicioAdministrarCentrosComunidad.actualizarImagenDeCentro(window.localStorage.getItem('idCentro'), this.listaImagenesDeCentro.map((imagen: any) => imagen.archivo)).then(()=>{
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.EMPLEADO_ANNADIDO);
            this.volverAAdministrarCentros();
          }).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_SUBIR_IMAGENES_CENTRO))
        }
      ).catch(() => this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, this.modoEditarCentro ? Errores.MENSAJES.ERROR_ACTUALIZAR_CENTRO : Errores.MENSAJES.ERROR_ANNADIR_CENTRO)); 
    } else {
      if(this.formulario.get("listadoAdministradores").invalid) {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.CENTRO_SIN_ADMINISTRADORES);
      } else {
        this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO);
      }
    }
  }

}
