import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-nueva-carpeta-de-pruebas',
  templateUrl: './nueva-carpeta-de-pruebas.component.html',
  styleUrls: ['./nueva-carpeta-de-pruebas.component.scss']
})
export class NuevaCarpetaDePruebas implements OnInit {
  nombreCarpeta: FormControl;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.nombreCarpeta = new FormControl({value: data ? data.nombre : '', disabled: false}, [Validators.required]);
  }

  ngOnInit(): void {
  }

}
