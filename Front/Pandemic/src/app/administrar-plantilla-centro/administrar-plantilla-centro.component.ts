import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import Errores from 'src/constantes/errores';
import Globales, { esTextoMasGrandeQueContenedor } from 'src/constantes/globales';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAdministrarPlantilla } from './administrar-plantilla.service';
import { ConfirmacionEliminarEmpleado } from './confirmacion-eliminar-empleado/confirmacion-eliminar-empleado.component';

@Component({
  selector: 'app-administrar-plantilla-centro',
  templateUrl: './administrar-plantilla-centro.component.html',
  styleUrls: ['./administrar-plantilla-centro.component.scss']
})
export class AdministrarPlantillaCentro implements OnInit {
  filtro: FormControl = new FormControl({value: '', disabled: false}, []);
  listadoPersonasPlantilla: any[] = [];
  listadoParaMostrar: MatTableDataSource<any> = new MatTableDataSource();
  columnasTabla: any[] = ['id', 'nombreCompleto', 'pruebasDisponibles', 'acciones'];
  opcionesPersonasPorPagina: number[] = [3, 6, 9]; 
  resultadosPorPagina: number = this.opcionesPersonasPorPagina[0];
  totalRegistros: number = 0;
  estaTextoReducido = esTextoMasGrandeQueContenedor;
  anchuraVentana: number;

  constructor(
    private enrutador: Router,
    private servicioPopUp: ServicioPopUp,
    private popUpConfirmacion: MatDialog,
    private servicioAdministrarPlantilla: ServicioAdministrarPlantilla,
  ) {
    this.cargarListados();
  }

  private cargarListados() {
    this.cargarEmpleadosCentro(0, this.resultadosPorPagina);
  }

  cargarEmpleadosCentro(indicePagina: number, resultadosPorPagina: number) {
    this.servicioAdministrarPlantilla.getPlantillaDeCentro(indicePagina, resultadosPorPagina)
      .then(
        async (pagina: any) => {
          this.totalRegistros = pagina.totalRegistros ? pagina.totalRegistros : 0;
          this.listadoPersonasPlantilla = await Promise.all(pagina.datos.map(async (persona: any) => {
            let pruebasDisponibles = "";
            await this.servicioAdministrarPlantilla.getPruebasQuePuedeTratarEmpleado(persona.usuario).then((listaNombrePruebas: any) => {
              listaNombrePruebas.forEach((nombrePrueba: string) => {
                pruebasDisponibles = pruebasDisponibles.length > 0 ?  pruebasDisponibles+", "+nombrePrueba : nombrePrueba;
              }) 
            });
            return {
              id: persona.id,
              nombre: persona.nombre,
              apellidos: persona.apellidos,
              nombreCompleto: persona.apellidos+", "+persona.nombre,
              pruebasDisponibles,
            }
          }));
          this.filtrarListado(this.filtro.value);
      })
      .catch(() => {this.listadoPersonasPlantilla = []; this.listadoParaMostrar.data = []; this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_EMPLEADOS);});
  }

  ngOnInit(): void {
  }

  modificarPagina(evento: any) {
    this.listadoParaMostrar.data = this.listadoPersonasPlantilla.slice(
      evento.pageIndex * evento.pageSize,
      (evento.pageIndex * evento.pageSize) + evento.pageSize,
    );
  }

  filtrarListado(cadena: string) {
    if (cadena) {
      this.listadoParaMostrar.data = this.listadoPersonasPlantilla.filter(
        (empleado) => empleado.id.toString().includes(cadena) || empleado.nombreCompleto.toLowerCase().includes(cadena.toLowerCase()) || empleado.pruebasDisponibles.toLowerCase().includes(cadena.toLowerCase())
      );
    } else {
      this.listadoParaMostrar.data = this.listadoPersonasPlantilla;
    }
  }

  cambioOrden(evento: any){
    this.listadoParaMostrar.data = this.listadoParaMostrar.data.sort((empleado1: any, empleado2: any) => {  
      if (typeof empleado1[evento.active] === 'string') {
        return evento.direction === 'asc' ? empleado1[evento.active].toLowerCase().localeCompare(empleado2[evento.active].toLowerCase()) : empleado2[evento.active].toLowerCase().localeCompare(empleado1[evento.active].toLowerCase());
      } else {
        return evento.direction === 'asc' ? empleado1[evento.active] - empleado2[evento.active] : empleado2[evento.active] - empleado1[evento.active];
      }
    }); 
  }

  confirmacionEliminarEmpleado(empleado: any) {
    if(empleado) {
      const popUp = this.popUpConfirmacion.open(ConfirmacionEliminarEmpleado, {
        width: '50%',
        minWidth: '370px',
        maxWidth: '800px',
        height: 'max-content',
        data: empleado,
        disableClose: true,
        autoFocus: false,
      });
  
      popUp.afterClosed().subscribe(confirmado => {
        if(confirmado) {
          this.servicioAdministrarPlantilla.eliminarEmpleado(empleado.id).then(() => {
            this.cargarEmpleadosCentro(0, this.resultadosPorPagina);
            this.servicioPopUp.open(Globales.TIPOS_POPUP.CORRECTO, Globales.MENSAJES.EMPLEADO_ELIMINADO);
          })
          .catch(() => {this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_ELIMINAR_EMPLEADO);});;
        }
      });
    }
  }

  annadirNuevoEmpleado() {
    window.localStorage.removeItem('idEmpleado');
    this.enrutador.navigate(['annadir-nuevo-empleado']);
  }

  editarEmpleado(empleado: any) {
    if(empleado) {
      window.localStorage.setItem('idEmpleado', empleado.id);
      this.enrutador.navigate(['actualizar-pruebas-empleado']);
    }
  }

  cambioAnchuraVentana() {
    this.anchuraVentana = document.body.clientWidth;
    if (this.anchuraVentana <= 490) {
      this.columnasTabla = ['nombreCompleto', 'pruebasDisponibles', 'acciones'];
    } else {
      this.columnasTabla = ['id', 'nombreCompleto', 'pruebasDisponibles', 'acciones'];
    }
  }
}
