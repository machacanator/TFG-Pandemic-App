import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ServicioAplicacion } from '../servicio-app.service';

@Component({
  selector: 'app-menu-principal',
  templateUrl: './menu-principal.component.html',
  styleUrls: ['./menu-principal.component.scss']
})
export class MenuPrincipal implements OnInit {

  nombre: string;

  constructor(
    // private ruta: ActivatedRoute,
    private servicioAplicacion: ServicioAplicacion,
    ) {
    // this.ruta.data.subscribe((data) => console.log(data))
    this.nombre = this.servicioAplicacion.getNombreUsuario();
  }
  
  ngOnInit(): void {
    /*En este componente solo rellenamos el cuadro con lo de nievenido y poco mas*/ 
    /*Añadir los menus en appcomponent y pedir los menus con la misma llamada que el servicio de tokens*/
  }

}
