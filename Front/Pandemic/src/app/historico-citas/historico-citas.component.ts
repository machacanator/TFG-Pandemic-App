import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import Globales from 'src/constantes/globales';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioHistoricoCitas } from './historico-citas.service';

@Component({
  selector: 'app-historico-citas',
  templateUrl: './historico-citas.component.html',
  styleUrls: ['./historico-citas.component.scss']
})
export class HistoricoCitas implements OnInit {

  filtro: FormControl = new FormControl({value: '', disabled: false}, []);
  listadoCitas: any[] = [];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  modoUsuario: boolean = true;
  columnasTabla: any[];
  opcionesCitasPorPagina: number[] = [3, 6, 9]; 
  resultadosPorPagina: number = this.opcionesCitasPorPagina[0];
  totalRegistros: number = 0;

  constructor(
    private servicioAplicacion: ServicioAplicacion,
    private servicioHistorico: ServicioHistoricoCitas,
  ) {
    this.modoUsuario = window.localStorage.getItem('modo-historico') === 'usuario';
    this.columnasTabla = this.modoUsuario
      ? ['id', 'fecha', 'hora', 'centro', 'prueba']
      : ['id', 'nombreCompleto', 'fecha', 'hora', 'centro', 'prueba'];
  }

  private cargarListados() {
    this.cargarCitas(0, this.resultadosPorPagina);
  }

  cargarCitas(indicePagina: number, resultadosPorPagina: number) {
    this.servicioHistorico.getHistoricoDeCitas(window.localStorage.getItem('modo-historico'), indicePagina, resultadosPorPagina)
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
    
    this.cargarListados();
  }

  modificarPagina(evento: any) {
    this.listadoParaMostrar.data = this.listadoCitas.slice(
      evento.pageIndex * evento.pageSize,
      (evento.pageIndex * evento.pageSize) + evento.pageSize,
    );
  }

  filtrarListado(cadena: string) {
    if (cadena) {
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
}
