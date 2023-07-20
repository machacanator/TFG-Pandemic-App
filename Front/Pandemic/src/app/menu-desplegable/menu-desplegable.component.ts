import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import Globales from 'src/constantes/globales';
import { ServicioAplicacion } from '../servicio-app.service';

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'menu-desplegable',
  templateUrl: './menu-desplegable.component.html',
  styleUrls: ['./menu-desplegable.component.scss']
})
export class MenuDesplegableComponent implements OnChanges {
  @Input() mostrarMenuDesplegable: boolean = false;
  @Output() cambiarIconosCabecera: EventEmitter<any>;
  
  opcionesMenu: any[];
  seccionSeleccionada: HTMLElement;
  tieneRoles: boolean;


  constructor(
    private enrutador: Router,
    private servicioAplicacion: ServicioAplicacion,
  ) {
    this.cambiarIconosCabecera = new EventEmitter<any>();
    this.opcionesMenu = this.cargarSecciones(this.servicioAplicacion.seccionesMenu);
  }
  
  private cargarSecciones(secciones: any[]): any {
    return secciones.map((seccion: any) => {
      let datosSeccion = Globales.MENU_OPCIONES[seccion.id];
      return {
        id: seccion.id,
        accesoLibre: seccion.accesoLibre,
        titulo: datosSeccion ? datosSeccion.titulo : '',
        url: datosSeccion ? datosSeccion.url : '',
        subsecciones: seccion.subSecciones.length > 0 && datosSeccion ? this.cargarSecciones(seccion.subSecciones) : seccion.Subsecciones,
      }
    });
  }

  // private getSeccionPorId(idSeccion: number) {
  //   return this.opcionesMenu.find((seccion: any) => {
  //     if (seccion.id === idSeccion) {
  //       return true;
  //     }
  //     if (seccion.subsecciones.length > 0) {
  //       return seccion.subsecciones.find((subseccion: any) => {
  //         return subseccion.id === idSeccion;
  //       }); 
  //     }
  //     return false;
  //   })
  // }

  
  private mostrarTitulosSecciones() {
    document.querySelectorAll('.seccion__titulo')?.forEach((htmlTitulo, i) => {
      htmlTitulo.classList.add('seccion__titulo--mostrar');
    });
  }

  private ocultarTitulosSecciones() {
    document.querySelectorAll('.seccion__titulo')?.forEach((htmlTitulo, i) => {
      htmlTitulo.classList.remove('seccion__titulo--mostrar');
    });
  }

  private desplegarMenu() {
    document.getElementById('menuDesplegable')?.classList.add("menu-desplegable--desplegado");
    setTimeout(() => this.mostrarTitulosSecciones(), 600);
  }
  
  private replegarMenu() {
    if (this.seccionSeleccionada) {
      this.seccionSeleccionada.classList.remove("seccion--seleccionada");
      // Esperar a que termine la animacion de cierre de la seccion seleccionada y luego cerrar el menu
      setTimeout(() => {this.cerrarMenu()}, 500);  
      this.seccionSeleccionada = null;
    } else {
      this.cerrarMenu();
    }
  }
  
  private cerrarMenu() {
    // Avisa al componente principal de activar la animacion de replegar del boton menu y el logo con un delay
    this.cambiarIconosCabecera.emit(); 
    document.getElementById('menuDesplegable')?.classList.remove("menu-desplegable--desplegado");
    this.ocultarTitulosSecciones();
  }

  ngOnChanges(changes: SimpleChanges): void {
    // this.tieneRoles = false;
    this.tieneRoles = this.servicioAplicacion.roles.length > 0;
    if(this.mostrarMenuDesplegable){
      this.desplegarMenu();
    } else {
      this.replegarMenu();
    }
  }
  
  getOpcionesMenuAccesoLibre() {
    return this.opcionesMenu.filter((seccion: any) => seccion.accesoLibre);
  }

  getOpcionesMenuAccesoRestringido() {
    return this.opcionesMenu.filter((seccion: any) => !seccion.accesoLibre);
  }

  seleccionarSeccion(seccion: any, evento: any) { /*Se llama dos veces al hacer click en una subseccion, mirar stoppropagation*/
    let esSubseccion = !!seccion.url;
    if (esSubseccion) {
      if(seccion.url === 'nueva-cita') window.localStorage.removeItem('idCita');
      // Si va a algun historico se recarga el componente dado que los 3 historicos comparten el mismo componente
      if(seccion.url.includes('historico-citas')) {
        window.localStorage.setItem('modo-historico', seccion.url.split('-').slice(-1)[0]);
        this.enrutador.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.enrutador.navigate(['historico-citas']);
        });
      } else {
        this.enrutador.navigate([seccion.url]);
      }
      this.replegarMenu();
    } else {
      // Si ya habia una seccion seleccionada se deselecciona
      if (this.seccionSeleccionada) {
        this.seccionSeleccionada.classList.remove("seccion--seleccionada");
      }
      let htmlSeccion = evento.target?.classList?.contains("seccion")
        ? evento.target
        : evento.target.parentNode;
      htmlSeccion.classList.add("seccion--seleccionada");
      this.seccionSeleccionada = htmlSeccion;
    }
    evento.stopPropagation();
  }
}
