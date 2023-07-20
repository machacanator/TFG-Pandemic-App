import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import Globales from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioCitasPendientes } from './citas-pendientes.service';
import { ConfirmacionCancelarCita } from './confirmacion-cancelar-cita/confirmacion-cancelar-cita.component';

@Component({
  selector: 'citas-pendientes',
  templateUrl: './citas-pendientes.component.html',
  styleUrls: ['./citas-pendientes.component.scss']
})
export class CitasPendientes implements OnInit {
  filtro: FormControl = new FormControl({value: '', disabled: false}, []);
  listadoCitas: any[] = [];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  columnasTabla: any[] = ['id', 'fecha', 'hora', 'centro', 'prueba', 'acciones'];
  opcionesCitasPorPagina: number[] = [3, 6, 9]; 
  resultadosPorPagina: number = this.opcionesCitasPorPagina[0];
  totalRegistros: number = 0;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private popUpConfirmacion: MatDialog,
    private servicioAplicacion: ServicioAplicacion,
    private servicioCitasPendientes: ServicioCitasPendientes,
  ) {
    this.cargarListados();
  }

  private cargarListados() {
    this.cargarCitas(0, this.resultadosPorPagina);
  }

  cargarCitas(indicePagina: number, resultadosPorPagina: number) {
    this.servicioCitasPendientes.getCitasPendientes(indicePagina, resultadosPorPagina)
      .then(
        (pagina: any) => {
          this.totalRegistros = pagina.totalRegistros ? pagina.totalRegistros : 0;
          this.listadoCitas = pagina.datos.map((cita: any) => {
            let centro = this.servicioAplicacion.listadoCentros.find((centro) => centro.id === cita.centro);
            let prueba = this.servicioAplicacion.listadoPruebas.find((prueba) => prueba.id === cita.prueba);
            let fecha = moment(cita.fecha);
            return {
              ...cita,
              fecha: fecha.toDate().toLocaleDateString(),
              hora: fecha.format(Globales.FORMATO_HORA),
              fecha_hora: fecha,
              centro: centro ? centro.nombre : '',
              idCentro: cita.centro,
              prueba: prueba ? prueba.nombre : '',
              idPrueba: cita.prueba,
            }
          });
          this.filtrarListado(this.filtro.value);
      })
      .catch(() => {this.listadoCitas = []; this.listadoParaMostrar.data = []; Errores.MENSAJES.ERROR_LISTADO_CITAS});
  }

  ngOnInit(): void {
  }

  modificarPagina(evento: any) {
    this.listadoParaMostrar.data = this.listadoCitas.slice(
      evento.pageIndex * evento.pageSize,
      (evento.pageIndex * evento.pageSize) + evento.pageSize,
    );
  }

  filtrarListado(cadena: string) {
    if (cadena) {
      console.log(this.listadoCitas)
      this.listadoParaMostrar.data = this.listadoCitas.filter(
        (cita) => cita.id.toString().includes(cadena) || cita.fecha.includes(cadena) || cita.hora.includes(cadena)
          || cita.centro.toLowerCase().includes(cadena.toLowerCase()) || cita.prueba.toLowerCase().includes(cadena.toLowerCase())
      );
    } else {
      this.listadoParaMostrar.data = this.listadoCitas;
    }
  }

  cambioOrden(evento: any){
    this.listadoParaMostrar.data = this.listadoParaMostrar.data.sort((cita1: any, cita2: any) => {
      if (evento.active === 'fecha') return evento.direction === 'asc' ? cita1.fecha_hora - cita2.fecha_hora : cita2.fecha_hora - cita1.fecha_hora;
      if (typeof cita1[evento.active] === 'string') {
        return evento.direction === 'asc' ? cita1[evento.active].toLowerCase().localeCompare(cita2[evento.active].toLowerCase()) : cita2[evento.active].toLowerCase().localeCompare(cita1[evento.active].toLowerCase());
      } else {
        return evento.direction === 'asc' ? cita1[evento.active] - cita2[evento.active] : cita2[evento.active] - cita1[evento.active];
      }
    });
  }

  confirmacionEliminarCita(cita: any) {
    if(cita) {
      const popUp = this.popUpConfirmacion.open(ConfirmacionCancelarCita, {
        width: '50%',
        minWidth: '370px',
        maxWidth: '800px',
        height: 'max-content',
        data: cita,
        disableClose: true,
        autoFocus: false,
      });
  
      popUp.afterClosed().subscribe(confirmado => {
        if(confirmado) {
          this.servicioCitasPendientes.eliminarCita(cita.id).then(() => {
            this.cargarCitas(0, this.resultadosPorPagina);
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_BORRAR_CITA);});;
        }
      });
    }
  }

  editarCita(cita: any) {
    if(cita) {
      window.localStorage.setItem('idCita', cita.id);
      this.enrutador.navigate(['nueva-cita']);
    }
  }

}
