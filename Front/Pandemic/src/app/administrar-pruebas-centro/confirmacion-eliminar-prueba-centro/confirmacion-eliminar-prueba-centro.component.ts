import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmacion-eliminar-prueba-centro',
  templateUrl: './confirmacion-eliminar-prueba-centro.component.html',
  styleUrls: ['./confirmacion-eliminar-prueba-centro.component.scss']
})
export class ConfirmacionEliminarPruebaCentro implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
  }

}
