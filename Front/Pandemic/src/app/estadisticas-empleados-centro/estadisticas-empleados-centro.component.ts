import { AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDatepicker } from '@angular/material/datepicker';
import { Chart, registerables } from 'chart.js';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import Globales, { validadorFecha } from 'src/constantes/globales';
import { CabeceraCalendarioCitas } from '../cabecera-calendario-citas/cabecera-calendario-citas.component';
import { ServicioEstadisticasEnfermero } from '../estadisticas-enfermero/estadisticas-enfermero.service';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServicioEstadisticasCentro } from '../estadisticas-centro/estadisticas-centro.service';

@Component({
  selector: 'app-estadisticas-empleados-centro',
  templateUrl: './estadisticas-empleados-centro.component.html',
  styleUrls: ['./estadisticas-empleados-centro.component.scss']
})
export class EstadisticasEmpleadosCentro implements OnInit, AfterViewInit {
  @ViewChild('graficoBarrasVerticales') CTXGraficoBarrasVerticales: ElementRef;
  @ViewChildren('graficoCircular') CTXGraficosCirculares: QueryList<ElementRef>;
  @ViewChild('listadoCirculares') listadoCirculares: QueryList<ElementRef>;
  

  cabeceraCalendario = CabeceraCalendarioCitas;
  graficoBarrasVerticales: Chart = null;
  graficosCirculares: Chart[] = [];
  datosGraficos: any[] = null;
  listaEmpleadosCentro: any[] = [];
  listadoColoresPruebas: any[];
  listadoAnnosDisponibles: string[];
  formulario: FormGroup<any>;
  busquedaAnterior: any;
  filtroFechas = (fecha: moment.Moment) => (new Date().getFullYear() - fecha?.year()) < 5;

  constructor(
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioEstadisticasEnfermero: ServicioEstadisticasEnfermero,
    private servicioEstadisticasCentro: ServicioEstadisticasCentro,
  ) {
    this.pedirListadoEmpleados()
    this.iniciarListadoAnnos(); 
    this.iniciarFormulario();
    Chart.register(...registerables);
  }

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.CTXGraficosCirculares.changes.subscribe((changes) => {
      if(this.datosGraficos) {
        this.graficosCirculares = this.inicializarGraficosCirculares();
      }
    });
  }

  private iniciarListadoAnnos() {
    let fechaActual = new Date().getFullYear();
    this.listadoAnnosDisponibles = [];
    for(let anno = fechaActual; anno > fechaActual - 5; anno--) {
      this.listadoAnnosDisponibles.push(anno.toString());
    }
  }

  private iniciarFormulario() {
    let fechaActual: moment.Moment = moment();
    fechaActual.format(Globales.FORMATO_FECHA_NACIONAL);
    this.formulario = new FormGroup({
      idUsuarioEmpleado: new FormControl({value: 0, disabled: false}, [Validators.required, Validators.min(1)]),
      fecha: new FormControl({value: fechaActual, disabled: false}, [Validators.required, validadorFecha()]),
      anno: new FormControl({value: this.listadoAnnosDisponibles[0], disabled: false}, [Validators.required]), 
      modoAnual: new FormControl({value: false, disabled: false}, []),
    });
  }

  private iniciarListadoColoresPruebas() {
   this.listadoColoresPruebas = this.servicioAplicacion.listadoPruebas.map((prueba: any) => ({id: prueba.id, color: this.generarColorAleatorio()}));
  }

  private inicializarGraficoBarrasVerticales() {
    const opcionesGrafico: any = {
      scales: {
        x: {
          title: {
            display: true,
            text: this.busquedaAnterior.modoAnual
              ? 'Mes'
              : 'Día',
            color: '#607196',
            font: {
              family: '"Poppins", sans-serif',
              size: 20,
              weight: 'bold',
            },
          },
        },
        y: {
          title: {
            display: true,
            text: 'Número de citas',
            color: '#607196',
            font: {
              family: '"Poppins", sans-serif',
              size: 20,
              weight: 'bold',
              lineHeight: 2,
            },
          },
          max: this.getMaximoNumeroDeCitas(),
          ticks: {
            stepSize: 1,
          },
        },
      },
      plugins: {
        legend: {
          align: 'center',
          position: 'bottom',
          labels: {
            font: {
              family: '"Poppins", sans-serif',
              size: '13px',
              weight: 'bold',
            },
          },
        },
        tooltip: {
          mode: 'index',
          axis: 'x',
          intersect: this.datosGraficos?.length <= 1,
          filter: (data: any) => data.formattedValue > 0,
        },
      },
      responsive: true,
      maintainAspectRatio: false,
    };
    const datosGrafico = {
      labels: this.getEtiquetasGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_BARRAS_VERTICALES, null),
      datasets: this.datosGraficos?.map(
        (centro) => {
          const color = this.generarColorAleatorio();
          return {
            data: centro.totalCitas,
            label: centro.nombreCentro,
            backgroundColor: color,
            borderColor: undefined,
          };
        },
      ),
    };
    return new Chart(this.getCTXDeGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_BARRAS_VERTICALES, null), {
      type: this.getConfiguracionTipoGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_BARRAS_VERTICALES),
      options: opcionesGrafico,
      data: datosGrafico,
    });
  }

  private inicializarGraficosCirculares() {
    const opcionesGrafico: any = {
      plugins: {
        legend: {
          align: 'center',
          position: 'bottom',
          labels: {
            font: {
              family: '"Poppins", sans-serif',
              size: '13px',
              weight: 'bold',
            },
          },
        },
        tooltip: {
          mode: 'index',
          axis: 'x',
          intersect: this.datosGraficos?.length <= 1,
          filter: (data: any) => data.formattedValue > 0,
        },
      },
      responsive: true,
      maintainAspectRatio: false,
    };
    /*Se cargar un gráfico circular por centro*/
    let listadoGraficosCirculares = this.datosGraficos.map((centro: any, indiceGrafico: number) => {
      const datosGrafico = {
        labels: this.getEtiquetasGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR, indiceGrafico),
        datasets: [
          {
            data: centro.pruebas.map((prueba: any) => prueba.total),
            label: centro.nombreCentro,
            backgroundColor: centro.pruebas.map((prueba: any) => this.listadoColoresPruebas.find((infoPrueba)=> infoPrueba.id === prueba.idPrueba).color),
          },
        ]
      };
      return new Chart(this.getCTXDeGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR, indiceGrafico), {
        type: this.getConfiguracionTipoGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR),
        options: opcionesGrafico,
        data: datosGrafico,
      });
    });
    /* Si hay información de más de un centro se mostrará un gráfico adicional con la unión  de todos los centros*/
    if(this.datosGraficos.length > 1) {
      listadoGraficosCirculares.push(this.inicializarGraficoCircularTodosCentros(opcionesGrafico));
    }
    return listadoGraficosCirculares;
  }

  private inicializarGraficoCircularTodosCentros(opcionesGrafico: any) {
    let listadoIdsTodasPruebas: number[] = [];
    this.datosGraficos.forEach((centro: any) => {
      listadoIdsTodasPruebas = listadoIdsTodasPruebas.concat(centro.pruebas.filter((prueba: any) => !listadoIdsTodasPruebas.includes(prueba.idPrueba)).map((prueba: any) => prueba.idPrueba));
    }); 
    return new Chart(this.getCTXDeGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR, this.datosGraficos.length), {
      type: this.getConfiguracionTipoGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR),
      options: opcionesGrafico,
      data: {
        labels: listadoIdsTodasPruebas.map((idPrueba: number) => this.servicioAplicacion.listadoPruebas.find((infoPrueba: any) => infoPrueba.id === idPrueba)?.nombre),
        datasets: this.datosGraficos.map((centro: any) => {
          return {
            data: listadoIdsTodasPruebas.map((idPrueba: number) => {
              let indicePrueba:  number = centro.pruebas.findIndex((prueba: any) => prueba.idPrueba === idPrueba);
              return indicePrueba >= 0 ? centro.pruebas[indicePrueba].total : 0;
            }) ,
            label: centro.nombreCentro,
            backgroundColor: centro.pruebas.map((prueba: any) => this.listadoColoresPruebas.find((infoPrueba)=> infoPrueba.id === prueba.idPrueba).color),
          }
        }),
      },
    })
  }

  private getMaximoNumeroDeCitas(): number {
    let a: number[] = [];
    let maximoNumeroDeCitas = 0;
    this.datosGraficos.forEach((centro: any) => {
      centro.totalCitas.forEach((totalDeCitas: number) => {
        if(totalDeCitas > maximoNumeroDeCitas) maximoNumeroDeCitas = totalDeCitas;
      });
    });
    if(maximoNumeroDeCitas < 10) return 10;
    return maximoNumeroDeCitas % 2 === 0 ? maximoNumeroDeCitas + 2 : maximoNumeroDeCitas + 1;
  }

  private getConfiguracionTipoGrafico(tipoGrafico: any): any {
    switch (tipoGrafico) {
      case Globales.TIPOS_GRAFICOS.GRAFICO_BARRAS_VERTICALES: return 'bar';
      case Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR: return 'pie';
      default: return '';
    }
  }

  private getCTXDeGrafico(tipoGrafico: number, indiceGrafico: number) {
    switch (tipoGrafico) {
      case Globales.TIPOS_GRAFICOS.GRAFICO_BARRAS_VERTICALES: {
        return this.CTXGraficoBarrasVerticales.nativeElement.getContext('2d');
      }
      case Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR: {
        return this.CTXGraficosCirculares.get(indiceGrafico).nativeElement.getContext('2d');
      }
      default: return undefined;
    }
  }

  private getListadoDiasDelMesSeleccionado(): string[] {
    const diasDelMesSeleccionado:number = this.busquedaAnterior.fecha.daysInMonth();
    let listadoDias: any[] = [];
    for(let dia = 1; dia <= diasDelMesSeleccionado; dia++) {
      listadoDias.push(dia.toString());
    }
    return listadoDias;
  }

  private destruirGraficos() {
    this.graficoBarrasVerticales?.destroy();
    this.graficosCirculares?.map((graficoCircular: any) => graficoCircular.destroy());
  }

  private generarColorAleatorio():string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i+=1) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  private getEtiquetasGrafico(tipoGrafico: any, indice: number): string[] {
    if(tipoGrafico === Globales.TIPOS_GRAFICOS.GRAFICO_BARRAS_VERTICALES) {
      return this.busquedaAnterior.modoAnual ? Globales.LISTADO_MESES : this.getListadoDiasDelMesSeleccionado()
    } 
    return this.datosGraficos[indice].pruebas.map((prueba: any) => this.servicioAplicacion.listadoPruebas.find((infoPrueba) => infoPrueba.id === prueba.idPrueba)?.nombre)
  }

  private pedirListadoEmpleados() {
    this.servicioEstadisticasCentro.getEmpleadosCentro()
      .then((listadoEmpleados: any) => { this.listaEmpleadosCentro = listadoEmpleados; })
      .catch(() => {this.listaEmpleadosCentro = []; this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_LISTADO_EMPLEADOS);})
  }

  private generarDatosGraficoGrande() {
    return [
        {
          nombreCentro: 'Hospital de la Princesa',
          totalCitas: this.busquedaAnterior.modoAnual ? [132, 112, 94, 96, 82, 92, 58, 61, 134, 110, 85, 136] : [0, 0, 5, 7, 4, 9, 6, 0, 0, 9, 9, 7, 2, 5, 0, 0, 4, 6, 3, 8, 7, 0, 0, 4, 5, 2, 2, 9, 0, 0],
          pruebas: this.busquedaAnterior.modoAnual ? [{idPrueba: 1, total: 170}, {idPrueba: 2, total: 67}, {idPrueba: 3, total: 159}, {idPrueba: 4, total: 439}, {idPrueba: 5, total: 203}, {idPrueba: 6, total: 105}, {idPrueba: 7, total: 49}] : [{idPrueba: 1, total: 15}, {idPrueba: 2, total: 9}, {idPrueba: 3, total: 15}, {idPrueba: 4, total: 29}, {idPrueba: 5, total: 24}, {idPrueba: 6, total: 13}, {idPrueba: 7, total: 8}]
        },
        {
          nombreCentro: 'Hospital Universitario de Getafe',
          totalCitas: this.busquedaAnterior.modoAnual ? [131, 121, 71, 60, 69, 75, 57, 53, 90, 105, 117, 137] : [0, 0, 1, 5, 5, 9, 6, 0, 0, 9, 10, 10, 4, 6, 0, 0, 10, 6, 5, 5, 8, 0, 0, 3, 8, 2, 7, 5, 0, 0],
          pruebas: this.busquedaAnterior.modoAnual ? [{idPrueba: 1, total: 146}, {idPrueba: 2, total: 11}, {idPrueba: 3, total: 168}, {idPrueba: 4, total: 206}, {idPrueba: 5, total: 255}, {idPrueba: 6, total: 147}, {idPrueba: 7, total: 78}] : [{idPrueba: 1, total: 17}, {idPrueba: 2, total: 17}, {idPrueba: 3, total: 18}, {idPrueba: 4, total: 24}, {idPrueba: 5, total: 28}, {idPrueba: 6, total: 16}, {idPrueba: 7, total: 10}]
        },
    ];
  }

  public cambioMes(mesNormalizado: moment.Moment, selectorFecha: MatDatepicker<moment.Moment>) {
    const valor = this.formulario.get("fecha").value;
    valor.month(mesNormalizado.month());
    this.formulario.get("fecha").setValue(valor);
    selectorFecha.close();
  }

  public cambioAnno(annoNormalizado: moment.Moment) {
    const valor = this.formulario.get("fecha").value;
    valor.year(annoNormalizado.year());
    this.formulario.get("fecha").setValue(valor);
  }

  public pedirEstadisticas() {
    this.formulario.markAllAsTouched();
    if (this.formulario.valid) {
      if (this.datosGraficos) {
        this.destruirGraficos();
      }
      this.busquedaAnterior = this.formulario.value;
      this.servicioEstadisticasCentro.getDatosGraficosEstadisticasEmpleado(
        parseInt(this.busquedaAnterior.idUsuarioEmpleado),
        this.formulario.get('modoAnual').value ? this.busquedaAnterior.anno : this.busquedaAnterior.fecha.format(Globales.FORMATO_FECHA_MES),
        this.formulario.get('modoAnual').value,
      ).then((datosGrafico: any) => {
        this.datosGraficos = datosGrafico;
        this.datosGraficos = this.generarDatosGraficoGrande();
        this.iniciarListadoColoresPruebas();
        this.graficoBarrasVerticales = this.inicializarGraficoBarrasVerticales();
      });
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO)
    }
  }
}
