import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import Globales, { esTextoMasGrandeQueContenedor } from 'src/constantes/globales';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioDiarioCitas } from './diario-citas.service';


@Component({
  selector: 'diario-citas',
  templateUrl: './diario-citas.component.html',
  styleUrls: ['./diario-citas.component.scss']
})
export class DiarioCitas implements OnInit {
  
  anchuraVentana: number;
  listadoCitas: any[] = [];
  totalRegistros: number = 0;
  opcionesCitasPorPagina: number[] = [3, 6, 9]; 
  resultadosPorPagina: number = this.opcionesCitasPorPagina[0];
  columnasTabla: any[] = ['id', 'estado', 'fecha', 'hora', 'centro', 'prueba'];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  filtroCadena: FormControl = new FormControl({value: '', disabled: false}, []);
  filtroPrueba: FormControl = new FormControl({value: '', disabled: false}, []);
  filtroCentro: FormControl = new FormControl({value: '', disabled: false}, []);
  estaTextoReducido = esTextoMasGrandeQueContenedor;

  constructor(
    private enrutador: Router,
    private servicioCitasHoy: ServicioDiarioCitas,
    public servicioAplicacion: ServicioAplicacion,
  ) {
    this.cargarListados();
  }

  private cargarListados() {
    this.cargarCitas(0, this.resultadosPorPagina);
  }

  cargarCitas(indicePagina: number, resultadosPorPagina: number) {
    this.servicioCitasHoy.getDiarioCitas(indicePagina, resultadosPorPagina)
      .then(
        (pagina: any) => {
          this.totalRegistros = pagina.totalRegistros ? pagina.totalRegistros : 0;
          this.listadoCitas = pagina.datos.map((cita: any) => {
            let centro = this.servicioAplicacion.listadoCentros.find((centro) => centro.id === cita.centro);
            let prueba = this.servicioAplicacion.listadoPruebas.find((prueba) => prueba.id === cita.prueba);
            let fecha = moment(cita.fecha);
            let cadenaEstado;
            switch(cita.estado) {
              case 1: cadenaEstado = 'Completada'; break;
              case 0: cadenaEstado = 'Pendiente'; break;
              case -3: cadenaEstado = 'Cancelada'; break;
              default: cadenaEstado = '';
            }
            return {
              ...cita,
              cadenaEstado,
              fecha: fecha.format(Globales.FORMATO_FECHA_NACIONAL).replaceAll('-', '/'),
              hora: fecha.format(Globales.FORMATO_HORA),
              fecha_hora: fecha,
              centro: centro ? centro.nombre : '',
              idCentro: cita.centro,
              prueba: prueba ? prueba.nombre : '',
              idPrueba: cita.prueba,
            }
          });
          this.filtrarListado();
      })
      .catch(() => {this.listadoCitas = []; this.listadoParaMostrar.data = []; Errores.MENSAJES.ERROR_LISTADO_CITAS});
  }

  ngOnInit(): void {
    this.cambioAnchuraVentana();
  }

  modificarPagina(evento: any) {
    this.listadoParaMostrar.data = this.listadoCitas.slice(
      evento.pageIndex * evento.pageSize,
      (evento.pageIndex * evento.pageSize) + evento.pageSize,
    );
  }

  filtrarListado() {
    this.listadoParaMostrar.data = this.listadoCitas;
    if (this.filtroPrueba.value > 0) {
      const idPrueba:number = this.filtroPrueba.value;
      this.listadoParaMostrar.data = this.listadoParaMostrar.data.filter(
        (cita) => cita.idPrueba === idPrueba
      );
    } 
    if (this.filtroCentro.value > 0) {
      const idCentro:number = this.filtroCentro.value;
      this.listadoParaMostrar.data = this.listadoParaMostrar.data.filter(
        (cita) => cita.idCentro === idCentro
      );
    }
    if (this.filtroCadena.value) {
      const cadena:string = this.filtroCadena.value;
      this.listadoParaMostrar.data = this.listadoParaMostrar.data.filter(
        (cita) => cita.id.toString().includes(cadena) || cita.cadenaEstado.toLowerCase().includes(cadena.toLowerCase()) || cita.fecha.includes(cadena) || cita.hora.includes(cadena)
          || cita.centro.toLowerCase().includes(cadena.toLowerCase()) || cita.prueba.toLowerCase().includes(cadena.toLowerCase())
      );
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

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
    if (this.anchuraVentana <= 490) {
      this.columnasTabla = ['estado', 'hora', 'centro', 'prueba'];
    } else if (this.anchuraVentana <= 800) {
      this.columnasTabla = ['estado', 'fecha', 'hora', 'centro', 'prueba'];
    } else {
      this.columnasTabla = ['id', 'estado', 'fecha', 'hora', 'centro', 'prueba'];
    }
  }

  confirmacionEliminarCita(cita: any) {}

  gestionarCita(cita: any) {
    if(cita) {
      window.localStorage.setItem('idCitaGestion', cita.id);
      this.enrutador.navigate(['gestionar-cita']);
    }
  }

}
