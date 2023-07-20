import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import Globales from 'src/constantes/globales';
import { PopUp } from './componente/pop-up.component';

@Injectable({
  providedIn: 'root'
})
export class ServicioPopUp {

  constructor(private componenteSnackBar: MatSnackBar) {
  }

  open(tipoPopUp: number, mensaje: string = '', duracionPopUpAbierto: number = 3000) {
    let css = 'estado-correcto';

    if (tipoPopUp === Globales.TIPOS_POPUP.AVISO) css = 'estado-aviso';
    if (tipoPopUp === Globales.TIPOS_POPUP.ERROR) css = 'estado-error';

    this.componenteSnackBar.openFromComponent(PopUp, {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: css,
      data: {
        tipo: tipoPopUp,
        mensaje: mensaje,
      },
      duration: duracionPopUpAbierto,
    });
  }
}
