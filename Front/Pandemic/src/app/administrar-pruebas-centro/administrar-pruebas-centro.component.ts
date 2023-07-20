import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { esTextoMasGrandeQueContenedor } from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioAdministrarPruebasCentro } from './administrar-pruebas-centro.service';
import { ConfirmacionEliminarPruebaCentro } from './confirmacion-eliminar-prueba-centro/confirmacion-eliminar-prueba-centro.component';

@Component({
  selector: 'app-administrar-pruebas-centro',
  templateUrl: './administrar-pruebas-centro.component.html',
  styleUrls: ['./administrar-pruebas-centro.component.scss']
})
export class AdministrarPruebasCentro implements OnInit {
  filtro: FormControl = new FormControl({value: '', disabled: false}, []);
  listadoPruebasCentro: any[] = [];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  columnasTabla: any[] = ['nombre', 'horarios', 'acciones'];
  opcionesPruebasPorPagina: number[] = [3, 6, 9]; 
  resultadosPorPagina: number = this.opcionesPruebasPorPagina[0];
  totalRegistros: number = 0;
  estaTextoReducido = esTextoMasGrandeQueContenedor;
  anchuraVentana: number;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private popUpConfirmacion: MatDialog,
    private servicioAplicacion: ServicioAplicacion,
    private servicioAdministrarPruebasCentro: ServicioAdministrarPruebasCentro,
  ) {
    this.cargarListados();
  }

  private cargarListados() {
    this.cargarPruebasComplementariasCentro(0, this.resultadosPorPagina);
  }

  cargarPruebasComplementariasCentro(indicePagina: number, resultadosPorPagina: number) {
    this.servicioAdministrarPruebasCentro.getPruebasYHorariosCentroDeAdmin(indicePagina, resultadosPorPagina)
      .then(
        async (pagina: any) => {
          this.totalRegistros = pagina.totalRegistros ? pagina.totalRegistros : 0;
          this.listadoPruebasCentro = pagina.datos.map((infoPrueba: any) => ({
            id: infoPrueba.idPrueba,
            nombre: this.servicioAplicacion.listadoPruebas.find((prueba: any) => prueba.id === infoPrueba.idPrueba).nombre,
            horarios: infoPrueba.horarios,
          }));
          this.filtrarListado(this.filtro.value);
      })
      .catch(() => {this.listadoPruebasCentro = []; this.listadoParaMostrar.data = []; Errores.MENSAJES.ERROR_LISTADO_PRUEBAS_CENTRO});
  }

  ngOnInit(): void {
  }

  modificarPagina(evento: any) {
    this.listadoParaMostrar.data = this.listadoPruebasCentro.slice(
      evento.pageIndex * evento.pageSize,
      (evento.pageIndex * evento.pageSize) + evento.pageSize,
    );
  }

  filtrarListado(cadena: string) {
    if (cadena) {
      this.listadoParaMostrar.data = this.listadoPruebasCentro.filter(
        (prueba) => prueba.nombre.toLowerCase().includes(cadena.toLowerCase()) || prueba.horarios.find((horario: any) => horario.horaInicio.includes(cadena) || horario.horaFin.includes(cadena))
      );
    } else {
      this.listadoParaMostrar.data = this.listadoPruebasCentro;
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

  confirmacionEliminarPruebaDeCentro(prueba: any) {
    if(prueba) {
      const popUp = this.popUpConfirmacion.open(ConfirmacionEliminarPruebaCentro, {
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
          this.servicioAdministrarPruebasCentro.eliminarPruebaDeCentro(prueba.id).then(() => {
            this.cargarPruebasComplementariasCentro(0, this.resultadosPorPagina);
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.PRUEBA_CENTRO_ELIMINADA);
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ELIMINAR_PRUEBA_CENTRO);});;
        }
      });
    }
  }

  annadirNuevaPruebaComplementariaACentro() {
    window.localStorage.removeItem('idPruebaCentro');
    this.enrutador.navigate(['annadir-nueva-prueba-centro']);
  }

  editarHorariosPruebaComplementaria(prueba: any) {
    if(prueba) {
      window.localStorage.setItem('idPruebaCentro', prueba.id);
      this.enrutador.navigate(['actualizar-horarios-prueba-centro']);
    }
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }
}
