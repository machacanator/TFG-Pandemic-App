import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { esTextoMasGrandeQueContenedor } from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAdministrarPruebasComunidad } from './administrar-pruebas-comunidad.service';
import { ConfirmacionEliminarCarpetaDePruebas } from './confirmacion-eliminar-carpeta-de-pruebas/confirmacion-eliminar-carpeta-de-pruebas.component';
import { NuevaCarpetaDePruebas } from './nueva-carpeta-de-pruebas/nueva-carpeta-de-pruebas.component';

@Component({
  selector: 'app-administrar-carpetas-pruebas-comunidad',
  templateUrl: './administrar-carpetas-pruebas-comunidad.component.html',
  styleUrls: ['./administrar-carpetas-pruebas-comunidad.component.scss']
})
export class AdministrarCarpetasPruebasComunidad implements OnInit {

  filtro: FormControl = new FormControl({value: '', disabled: false}, []);
  listadoCarpetasDePruebas: any[] = [];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  columnasTabla: any[] = ['id', 'nombre', 'acciones'];
  opcionesCarpetasPorPagina: number[] = [3, 6, 9]; 
  resultadosPorPagina: number = this.opcionesCarpetasPorPagina[0];
  totalRegistros: number = 0;
  anchuraVentana: number;
  estaTextoReducido = esTextoMasGrandeQueContenedor;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private ventana: MatDialog,
    private servicioAdministrarCarpetasDePruebas: ServicioAdministrarPruebasComunidad,
  ) {
    this.cargarCarpetasDePruebasDeLaComunidad(0, this.resultadosPorPagina);
  }

  cargarCarpetasDePruebasDeLaComunidad(indicePagina: number, resultadosPorPagina: number) {
    this.servicioAdministrarCarpetasDePruebas.getCarpetasDePruebasDeLaComunidad(indicePagina, resultadosPorPagina)
      .then(
        async (pagina: any) => {
          this.totalRegistros = pagina.totalRegistros ? pagina.totalRegistros : 0;
          this.listadoCarpetasDePruebas = pagina.datos;
          this.filtrarListado(this.filtro.value);
      })
      .catch(() => {this.listadoCarpetasDePruebas = []; this.listadoParaMostrar.data = []; Errores.MENSAJES.ERROR_LISTADO_CARPETAS_PRUEBAS});
  }

  ngOnInit(): void {
  }

  modificarPagina(evento: any) {
    this.listadoParaMostrar.data = this.listadoCarpetasDePruebas.slice(
      evento.pageIndex * evento.pageSize,
      (evento.pageIndex * evento.pageSize) + evento.pageSize,
    );
  }

  filtrarListado(cadena: string) {
    if (cadena) {
      this.listadoParaMostrar.data = this.listadoCarpetasDePruebas.filter(
        (carpetaDePruebas) => carpetaDePruebas.id.toString().includes(cadena) || carpetaDePruebas.nombre.toLowerCase().includes(cadena.toLowerCase())
      );
    } else {
      this.listadoParaMostrar.data = this.listadoCarpetasDePruebas;
    }
  }

  cambioOrden(evento: any){
    this.listadoParaMostrar.data = this.listadoParaMostrar.data.sort((carpetaDePruebas1: any, carpetaDePruebas2: any) => {  
      if (typeof carpetaDePruebas1[evento.active] === 'string') {
        return evento.direction === 'asc' ? carpetaDePruebas1[evento.active].toLowerCase().localeCompare(carpetaDePruebas2[evento.active].toLowerCase()) : carpetaDePruebas2[evento.active].toLowerCase().localeCompare(carpetaDePruebas1[evento.active].toLowerCase());
      } else {
        return evento.direction === 'asc' ? carpetaDePruebas1[evento.active] - carpetaDePruebas2[evento.active] : carpetaDePruebas2[evento.active] - carpetaDePruebas1[evento.active];
      }
    }); 
  }

  confirmacionEliminarCarpetaDePruebas(carpetaDePruebas: any) {
    if(carpetaDePruebas) {
      const popUp = this.ventana.open(ConfirmacionEliminarCarpetaDePruebas, {
        width: '50%',
        minWidth: '370px',
        maxWidth: '800px',
        data: carpetaDePruebas,
        disableClose: true,
        autoFocus: false,
      });
  
      popUp.afterClosed().subscribe(confirmado => {
        if(confirmado) {
          this.servicioAdministrarCarpetasDePruebas.eliminarCarpetaDePrueba(carpetaDePruebas.id).then(() => {
            this.cargarCarpetasDePruebasDeLaComunidad(0, this.resultadosPorPagina);
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.CARPETA_PRUEBAS_ELIMINADA);
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ELIMINAR_CARPETA_PRUEBAS);});;
        }
      });
    }
  }

  irAListadoPruebasDeCarpetaDePruebas(carpetaDePruebas: any) {
    window.localStorage.setItem('idCarpetaPruebas', carpetaDePruebas.id);
    this.enrutador.navigate(['administrar-pruebas-comunidad/pruebas']);
  }
  
  annadirNuevaCarpetaDePruebas() {
    this.ventana.open(NuevaCarpetaDePruebas, {
      width: '50%',
      minWidth: '370px',
      maxWidth: '800px',
      height: 'max-content',
      disableClose: true,
      autoFocus: false,
    }).afterClosed().subscribe((nombreCarpeta: string) => {
      if(nombreCarpeta !== null) {
        this.servicioAdministrarCarpetasDePruebas.annadirCarpetaDePruebas({nombre: nombreCarpeta})
          .then(() => {
            this.cargarCarpetasDePruebasDeLaComunidad(0, this.resultadosPorPagina);
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.CARPETA_PRUEBAS_ANNADIDA);
          })
          .catch(()=> {
            this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ANNADIR_CARPETA_PRUEBAS);
          })
      }
    });
  }

  editarCarpetaDePruebas(carpetaDePruebas: any) {
    this.ventana.open(NuevaCarpetaDePruebas, {
      width: '50%',
      minWidth: '370px',
      maxWidth: '800px',
      height: 'max-content',
      data: carpetaDePruebas,
      disableClose: true,
      autoFocus: false,
    }).afterClosed().subscribe((nombreCarpeta: string) => {
      if(nombreCarpeta !== null) {
        this.servicioAdministrarCarpetasDePruebas.actualizarCarpetaDePruebas({id: carpetaDePruebas.id, nombre: nombreCarpeta})
          .then(() => {
            this.cargarCarpetasDePruebasDeLaComunidad(0, this.resultadosPorPagina);
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.CARPETA_PRUEBAS_ACTUALIZADA);
          })
          .catch(()=> {
            this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ACTUALIZAR_CARPETA_PRUEBAS);
          })
      }
    });
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }

}
