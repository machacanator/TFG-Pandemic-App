import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ServicioPopUp } from 'src/app/popup/servicio-pop-up.service';
import { ServicioAplicacion } from 'src/app/servicio-app.service';
import Errores from 'src/constantes/errores';
import Globales from 'src/constantes/globales';
import { ServicioSesion } from '../servicio-sesion';

export function validadorContrasenna(): ValidatorFn { /*Cambiar validador*/
  return (control: AbstractControl): ValidationErrors | null => {
    let contieneMayusculas = new RegExp('.*[A-Z]+.*').test(control.value);
    let contieneNumeros = new RegExp('.*[0-9]+.*').test(control.value);
    let contieneCaracteresEspeciales = new RegExp('.*[^A-Za-z0-9]+.*').test(control.value);
    if (!contieneMayusculas) return {sinMayusculas: {value: control.value}}
    if (!contieneNumeros) return {sinNumeros: {value: control.value}}
    if (!contieneCaracteresEspeciales) return {sinCaracteresEspeciales: {value: control.value}}
    return null;
  };
}

@Component({
  selector: 'app-inicio-sesion',
  templateUrl: './inicio-sesion.component.html',
  styleUrls: ['./inicio-sesion.component.scss'],
})
export class InicioSesion implements OnInit {

  formularioInicioSesion: FormGroup;
  mostrarContrasenna: boolean;
  
  constructor(
    private router: Router,
    private popUp: ServicioPopUp,
    private servicioSesion: ServicioSesion,
    private servicioAplicacion: ServicioAplicacion,
  ) {
    if (ServicioAplicacion.estaLogueado()) {
      this.servicioAplicacion.cargarDatos().then(()=>{this.router.navigate(['']);});
    }
    this.inicializarFormulario();
  }
  
  ngOnInit(): void {
    this.mostrarContrasenna = false;
  }
  
  private inicializarFormulario() {
    this.formularioInicioSesion = new FormGroup({
      documentoIdentidad: new FormControl({value: '', disabled: false}, [Validators.required, Validators.pattern('[0-9]{8}[a-zA-Z]')]),
      contrasenna: new FormControl({value: '', disabled: false}, [Validators.required, Validators.minLength(7), validadorContrasenna()]),
    });
  }

  campoModificado(evento: KeyboardEvent) {
    if (evento.key === 'Enter') {
      this.enviarFormulario();
    }
  }

  enviarFormulario() {
    this.formularioInicioSesion.markAllAsTouched();
    if (this.formularioInicioSesion.valid) {
      this.servicioSesion.iniciarSesion(this.formularioInicioSesion.value).subscribe({
        next: (data) => {
          let token = data?.headers?.get(Globales.RESPUESTA.PARAMETRO_TOKEN);
          localStorage.setItem('token', token);
          this.servicioAplicacion.cargarDatos().then(()=>{this.router.navigate(['']);});
        },
        error: (error) => {
          console.log(error);
          console.log(error[Globales.RESPUESTA.PARAMETRO_ERROR_CODIGO]);
          if (error[Globales.RESPUESTA.PARAMETRO_ERROR_CODIGO] === Errores.CODIGOS_BACK.AUTENTICACION.codigo) {
            this.popUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.CREDENCIALES_ERRONEAS);
          } else {
            this.popUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_CONEXION);
          }
        }
    });
    } else {
      this.popUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.FORMULARIO_NO_VALIDO);
    }
  }
    
}
