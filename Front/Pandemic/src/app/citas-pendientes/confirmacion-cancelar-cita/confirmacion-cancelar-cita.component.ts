import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmacion-cancelar-cita',
  templateUrl: './confirmacion-cancelar-cita.component.html',
  styleUrls: ['./confirmacion-cancelar-cita.component.scss']
})
export class ConfirmacionCancelarCita implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
  }

}
