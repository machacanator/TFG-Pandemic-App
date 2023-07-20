import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { esTextoMasGrandeQueContenedor } from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioAdministrarCentrosComunidad } from './administrar-centros-comunidad.service';
import { ConfirmacionEliminarCentro } from './confirmacion-eliminar-centro/confirmacion-eliminar-centro.component';

@Component({
  selector: 'app-administrar-centros-comunidad',
  templateUrl: './administrar-centros-comunidad.component.html',
  styleUrls: ['./administrar-centros-comunidad.component.scss']
})
export class AdministrarCentrosComunidad implements OnInit {
  filtro: FormControl = new FormControl({value: '', disabled: false}, []);
  listadoTodosLosCentros: any[] = [];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  columnasTabla: any[] = ['nombre', 'localizacion', 'administradores', 'acciones'];
  estaTextoReducido = esTextoMasGrandeQueContenedor;
  anchuraVentana: number;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private popUpConfirmacion: MatDialog,
    private servicioAplicacion: ServicioAplicacion,
    private servicioAdministrarCentrosComunidad: ServicioAdministrarCentrosComunidad,
  ) {
    this.cargarListadoCentros();
  }

  async cargarListadoCentros() {
    await this.servicioAplicacion.getCentros();
    this.listadoTodosLosCentros = await Promise.all(this.servicioAplicacion.listadoCentros.map(async (centro: any) => {
      let administradores = "";
      await this.servicioAdministrarCentrosComunidad.getNombresAdministradoresCentro(centro.id).then((nombresAdministradores: any) => {
        administradores = nombresAdministradores.reduce((nombresAcumulados: string, nombreAdministrador: string, posicionArray: number) => posicionArray >  0 ? nombresAcumulados+", "+nombreAdministrador : nombreAdministrador, "");
      });
      return {
        id: centro.id,
        nombre: centro.nombre,
        localizacion: centro.direccion+", "+centro.codigoPostal+", "+centro.municipio,
        administradores,
      }
    }));
    this.filtrarListado(this.filtro.value);
  }

  ngOnInit(): void {
  }

  filtrarListado(cadena: string) {
    if (cadena) {
      this.listadoParaMostrar.data = this.listadoTodosLosCentros.filter(
        (centro) => centro.nombre.toLowerCase().includes(cadena.toLowerCase()) || centro.localizacion.toLowerCase().includes(cadena.toLowerCase()) || centro.administradores.toLowerCase().includes(cadena.toLowerCase())
      );
    } else {
      this.listadoParaMostrar.data = this.listadoTodosLosCentros;
    }
  }

  cambioOrden(evento: any){
    this.listadoParaMostrar.data = this.listadoParaMostrar.data.sort((centro1: any, centro2: any) => {  
      if (typeof centro1[evento.active] === 'string') {
        return evento.direction === 'asc' ? centro1[evento.active].toLowerCase().localeCompare(centro2[evento.active].toLowerCase()) : centro2[evento.active].toLowerCase().localeCompare(centro1[evento.active].toLowerCase());
      } else {
        return evento.direction === 'asc' ? centro1[evento.active] - centro2[evento.active] : centro2[evento.active] - centro1[evento.active];
      }
    }); 
  }

  confirmacionEliminarCentro(centro: any) {
    if(centro) {
      const popUp = this.popUpConfirmacion.open(ConfirmacionEliminarCentro, {
        width: '50%',
        minWidth: '370px',
        maxWidth: '800px',
        height: 'max-content',
        data: centro,
        disableClose: true,
        autoFocus: false,
      });
  
      popUp.afterClosed().subscribe(confirmado => {
        if(confirmado) {
          this.servicioAdministrarCentrosComunidad.eliminarCentro(centro.id).then(() => {
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.CENTRO_ELIMINADO)
            this.cargarListadoCentros();
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ELIMINAR_CENTRO);});;
        }
      });
    }
  }

  annadirNuevoCentro() {
    window.localStorage.removeItem('idCentro');
    this.enrutador.navigate(['annadir-centro-comunidad']);
  }

  editarCentro(centro: any) {
    if(centro) {
      window.localStorage.setItem('idCentro', centro.id);
      this.enrutador.navigate(['actualizar-centro-comunidad']);
    }
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
  }
}
