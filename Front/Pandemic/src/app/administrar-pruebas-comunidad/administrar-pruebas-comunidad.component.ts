import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { esTextoMasGrandeQueContenedor } from 'src/constantes/globales';
import { ServicioAdministrarPruebasComunidad } from '../administrar-carpetas-pruebas-comunidad/administrar-pruebas-comunidad.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ConfirmacionEliminarPrueba } from './confirmacion-eliminar-prueba/confirmacion-eliminar-prueba.component';

@Component({
  selector: 'app-administrar-pruebas-comunidad',
  templateUrl: './administrar-pruebas-comunidad.component.html',
  styleUrls: ['./administrar-pruebas-comunidad.component.scss']
})
export class AdministrarPruebasComunidad implements OnInit {

  filtro: FormControl = new FormControl({value: '', disabled: false}, []);
  listadoPruebas: any[] = [];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  columnasTabla: any[] = ['id', 'nombre', 'acciones'];
  opcionesPruebasPorPagina: number[] = [3, 6, 9]; 
  resultadosPorPagina: number = this.opcionesPruebasPorPagina[0];
  totalRegistros: number = 0;
  anchuraVentana: number;
  idCarpetaDeLasPruebas: number | string = null;
  estaTextoReducido = esTextoMasGrandeQueContenedor;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private popUpConfirmacion: MatDialog,
    private servicioAdministrarPruebas: ServicioAdministrarPruebasComunidad,
  ) {
    this.idCarpetaDeLasPruebas = window.localStorage.getItem('idCarpetaPruebas');
    this.cargarListados();
  }

  private cargarListados() {
    this.cargarPruebasDeLaCarpetaDePruebas(0, this.resultadosPorPagina, this.idCarpetaDeLasPruebas);
  }

  volverAListadoCarpetasDePruebas() {
    this.enrutador.navigate(['administrar-pruebas-comunidad/carpetas-pruebas']);
  }

  cargarPruebasDeLaCarpetaDePruebas(indicePagina: number, resultadosPorPagina: number, idCarpetaDePruebas: number | string) {
    this.servicioAdministrarPruebas.getPruebasDeLaCarpetaDePruebas(indicePagina, resultadosPorPagina, idCarpetaDePruebas)
      .then(
        async (pagina: any) => {
          this.totalRegistros = pagina.totalRegistros ? pagina.totalRegistros : 0;
          this.listadoPruebas = pagina.datos;
          this.filtrarListado(this.filtro.value);
      })
      .catch(() => {this.listadoPruebas = []; this.listadoParaMostrar.data = []; Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_COMUNIDAD});
  }

  ngOnInit(): void {
  }

  modificarPagina(evento: any) {
    this.listadoParaMostrar.data = this.listadoPruebas.slice(
      evento.pageIndex * evento.pageSize,
      (evento.pageIndex * evento.pageSize) + evento.pageSize,
    );
  }

  filtrarListado(cadena: string) {
    if (cadena) {
      this.listadoParaMostrar.data = this.listadoPruebas.filter(
        (carpetaDePruebas) => carpetaDePruebas.id.toString().includes(cadena) || carpetaDePruebas.nombre.toLowerCase().includes(cadena.toLowerCase())
      );
    } else {
      this.listadoParaMostrar.data = this.listadoPruebas;
    }
  }

  cambioOrden(evento: any){
    this.listadoParaMostrar.data = this.listadoParaMostrar.data.sort((prueba1: any, prueba2: any) => {  
      if (typeof prueba1[evento.active] === 'string') {
        return evento.direction === 'asc' ? prueba1[evento.active].toLowerCase().localeCompare(prueba2[evento.active].toLowerCase()) : prueba2[evento.active].toLowerCase().localeCompare(prueba1[evento.active].toLowerCase());
      } else {
        return evento.direction === 'asc' ? prueba1[evento.active] - prueba2[evento.active] : prueba2[evento.active] - prueba1[evento.active];
      }
    }); 
  }

  confirmacionEliminarPrueba(prueba: any) {
    if(prueba) {
      const popUp = this.popUpConfirmacion.open(ConfirmacionEliminarPrueba, {
        width: '50%',
        minWidth: '370px',
        maxWidth: '800px',
        height: 'max-content',
        data: prueba,
        disableClose: true,
        autoFocus: false,
      });
  
      popUp.afterClosed().subscribe(confirmado => {
        if(confirmado) {
          this.servicioAdministrarPruebas.eliminarPrueba(prueba.id).then(() => {
            this.cargarPruebasDeLaCarpetaDePruebas(0, this.resultadosPorPagina, this.idCarpetaDeLasPruebas);
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.PRUEBA_ELIMINADA);
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ELIMINAR_PRUEBA_COMUNIDAD);});;
        }
      });
    }
  }

  annadirNuevaPrueba() {
    window.localStorage.removeItem('idPrueba');
    this.enrutador.navigate(['administrar-pruebas-comunidad/nueva-prueba']);
  }

  editarPrueba(prueba: any) {
    if(prueba) {
      window.localStorage.setItem('idPrueba', prueba.id);
      this.enrutador.navigate(['administrar-pruebas-comunidad/actualizar-prueba']);
    }
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }

}
