import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmacion-eliminar-prueba',
  templateUrl: './confirmacion-eliminar-prueba.component.html',
  styleUrls: ['./confirmacion-eliminar-prueba.component.scss']
})
export class ConfirmacionEliminarPrueba implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
  }

}
