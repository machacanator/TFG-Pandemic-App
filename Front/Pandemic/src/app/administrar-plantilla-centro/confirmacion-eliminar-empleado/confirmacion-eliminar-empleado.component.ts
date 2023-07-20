import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmacion-eliminar-empleado',
  templateUrl: './confirmacion-eliminar-empleado.component.html',
  styleUrls: ['./confirmacion-eliminar-empleado.component.scss']
})
export class ConfirmacionEliminarEmpleado implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
  }

}
