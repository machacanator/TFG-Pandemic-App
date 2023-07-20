import { animate, style, transition, trigger } from '@angular/animations';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';
import Globales from 'src/constantes/globales';

@Component({
  selector: 'app-pop-up',
  templateUrl: './pop-up.component.html',
  styleUrls: ['./pop-up.component.scss'],
  /*animations: [
    trigger(
      'animacionPersonalizada',
      [
        transition(':enter', [
          style({top: '100px', transform: 'translate(-50%, 0%) scale(0.3)'}),
          animate(
            '150ms cubic-bezier(0, 0, 0.2, 1)',
            style({
              transform: 'translate(-50%, 0%) scale(1)',
              opacity: 1,
              bottom: '20px'
            }),
          ),
        ]),
        transition(':leave', [
          animate(
            '150ms cubic-bezier(0.4, 0.0, 1, 1)',
            style({
              transform: 'translate(-50%, 0%) scale(0.3)',
              opacity: 0,
              bottom: '-100px'
            }),
          ),
        ]),
      ],
    ),
  ],*/
})
export class PopUp implements OnInit {

  tiposPopUp =  Globales.TIPOS_POPUP;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public propiedades: any) { }

  ngOnInit(): void {
  }

}
