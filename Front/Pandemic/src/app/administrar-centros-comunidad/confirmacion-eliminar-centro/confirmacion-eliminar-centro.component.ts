import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmacion-eliminar-centro',
  templateUrl: './confirmacion-eliminar-centro.component.html',
  styleUrls: ['./confirmacion-eliminar-centro.component.scss']
})
export class ConfirmacionEliminarCentro implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
  }

}
