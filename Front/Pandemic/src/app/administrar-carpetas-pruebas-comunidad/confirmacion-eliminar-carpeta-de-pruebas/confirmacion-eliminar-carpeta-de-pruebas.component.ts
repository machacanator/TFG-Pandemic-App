import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmacion-eliminar-carpeta-de-pruebas',
  templateUrl: './confirmacion-eliminar-carpeta-de-pruebas.component.html',
  styleUrls: ['./confirmacion-eliminar-carpeta-de-pruebas.component.scss']
})
export class ConfirmacionEliminarCarpetaDePruebas implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
  }

}
