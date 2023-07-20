import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDatepicker } from '@angular/material/datepicker';
import { Chart, registerables } from 'chart.js';
import * as moment from 'moment';
import Errores from 'src/constantes/errores';
import Globales, { validadorFecha } from 'src/constantes/globales';
import { CabeceraCalendarioCitas } from '../cabecera-calendario-citas/cabecera-calendario-citas.component';
import { ServicioPopUp } from '../popup/servicio-pop-up.service';
import { ServicioAplicacion } from '../servicio-app.service';
import { ServiciosEstadisticasComunidad } from './estadisticas-comunidad.service';

@Component({
  selector: 'app-estadisticas-comunidad',
  templateUrl: './estadisticas-comunidad.component.html',
  styleUrls: ['./estadisticas-comunidad.component.scss']
})
export class EstadisticasComunidad implements OnInit {

  @ViewChild('graficoLineas') CTXGraficoLineas: ElementRef;
  @ViewChild('graficoLineasCarpeta') CTXGraficoLineasCarpeta: ElementRef;
  @ViewChild('graficoLineasPrueba') CTXGraficoLineasPrueba: ElementRef;
  @ViewChild('graficoCircular') CTXGraficoCircular: ElementRef;
  @ViewChild('graficoCircularCarpeta') CTXGraficoCircularCarpeta: ElementRef;
  @ViewChild('graficoCircularPrueba') CTXGraficoCircularPrueba: ElementRef;

  cabeceraCalendario = CabeceraCalendarioCitas;
  graficoLineasTotal: Chart = null;
  graficoLineasCarpeta: Chart = null;
  graficoLineasPrueba: Chart = null;
  graficoCircularTotal: Chart = null;
  graficoCircularCarpeta: Chart = null;
  graficoCircularPrueba: Chart = null;
  listadoColoresCentros: any[];
  busquedaAnterior: any;
  ultimaCarpetaFiltrada: any;
  ultimaPruebaFiltrada: any;
  listadoAnnosDisponibles: string[];
  nombreCentro: string = "";
  formulario: FormGroup<any>;
  datosGraficoTotal: any[] = null;
  datosGraficoFiltradoPorCarpeta: any[] = null;
  datosGraficoFiltradoPorPrueba: any[] = null;
  listaPruebasFiltro: any[];
  listaCarpetasFiltro: any[];
  selectorPrueba: FormControl;
  selectorCarpeta: FormControl;
  filtroFechas = (fecha: moment.Moment) => (new Date().getFullYear() - fecha?.year()) < 5;

  constructor (
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioEstadisticasComunidad: ServiciosEstadisticasComunidad,
  ) {
    this.listaPruebasFiltro = this.servicioAplicacion.listadoPruebas;
    this.listaCarpetasFiltro = this.servicioAplicacion.listadoCarpetasPruebas;
    this.iniciarListadoAnnos(); 
    this.iniciarFormulario();
    Chart.register(...registerables);
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
      fecha: new FormControl({value: fechaActual, disabled: false}, [Validators.required, validadorFecha()]),
      anno: new FormControl({value: this.listadoAnnosDisponibles[0], disabled: false}, [Validators.required]), 
      modoAnual: new FormControl({value: false, disabled: false}, []),
    });
    
    this.selectorPrueba = new FormControl({value: '', disabled: false}, [Validators.required]);
    this.selectorCarpeta = new FormControl({value: '', disabled: false}, [Validators.required]);
    this.ultimaPruebaFiltrada = null;
  }
  
  private iniciarListadoColoresCentros() {
    this.listadoColoresCentros = this.servicioAplicacion.listadoCentros.map((centro: any) => ({id: centro.id, color: this.generarColorAleatorio()}));
  }
  
  private inicializarGraficoLineas(tipoDatos: string) {
    let listaDeDatos = null;
    switch (tipoDatos) {
      case "CARPETAS": listaDeDatos = this.datosGraficoTotal; break;
      case "CARPETA": listaDeDatos = this.datosGraficoFiltradoPorCarpeta; break;
      case "PRUEBA": listaDeDatos = this.datosGraficoFiltradoPorPrueba; break;
    }
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
          max: this.getMaximoNumeroDeCitas(listaDeDatos),
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
          intersect: listaDeDatos?.length <= 1,
          filter: (data: any) => data.formattedValue > 0,
        },
      },
      responsive: true,
      maintainAspectRatio: false,
    };
    const datosGrafico = {
      labels: this.getEtiquetasGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS, tipoDatos),
      datasets: listaDeDatos?.map(
        (centro) => {
          const color = this.listadoColoresCentros.find((infoCentro)=> infoCentro.id === centro.idCentro).color;
          return {
            data: centro.citas,
            label: centro.nombreCentro,
            // backgroundColor: color,
            backgroundColor: color,
            borderColor: color,
          };
        },
      ),
    };
    console.log(datosGrafico)
    return new Chart(this.getCTXDeGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS, tipoDatos), {
      type: this.getConfiguracionTipoGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS),
      options: opcionesGrafico,
      data: datosGrafico,
    });
  }
  
  private inicializarGraficoCircular(tipoDatos: string) {
    let listaDeDatos = null;
    switch (tipoDatos) {
      case "CARPETAS": listaDeDatos = this.datosGraficoTotal; break;
      case "CARPETA": listaDeDatos = this.datosGraficoFiltradoPorCarpeta; break;
      case "PRUEBA": listaDeDatos = this.datosGraficoFiltradoPorPrueba; break;
    }
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
          intersect: listaDeDatos?.length <= 1,
          filter: (data: any) => data.formattedValue > 0,
        },
      },
      responsive: true,
      maintainAspectRatio: false,
    };
    const datosGrafico = {
      labels: this.getEtiquetasGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR, tipoDatos),
      datasets: [
        {
          data: listaDeDatos?.map((datosCentro: any) => datosCentro.citas.reduce((totalCitas: number, numeroDeCitas: number) => totalCitas + numeroDeCitas, 0)),
          label: listaDeDatos?.map((datosCentro: any) => datosCentro.nombreCentro),
          backgroundColor: listaDeDatos?.map((centro: any) => this.listadoColoresCentros.find((infoCentro)=> infoCentro.id === centro.idCentro).color),
        },
      ]
    };

    return  new Chart(this.getCTXDeGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR, tipoDatos), {
      type: this.getConfiguracionTipoGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR),
      options: opcionesGrafico,
      data: datosGrafico,
    });
  }

  private getMaximoNumeroDeCitas(listaDatos: any): number {
    let maximoNumeroDeCitas = 0;
    listaDatos.forEach((centro: any) => {
      centro.citas.forEach((citasCentro: number) => {
        if(citasCentro > maximoNumeroDeCitas) maximoNumeroDeCitas = citasCentro;
      });
    });
    if(maximoNumeroDeCitas < 10) return 10;
    return maximoNumeroDeCitas % 2 === 0 ? maximoNumeroDeCitas + 2 : maximoNumeroDeCitas + 1;
  }

  private getConfiguracionTipoGrafico(tipoGrafico: any): any {
    switch (tipoGrafico) {
      case Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR: return 'pie';
      case Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS: return 'line';
      default: return '';
    }
  }

  private getCTXDeGrafico(tipoGrafico: number, tipoDatos: string) {
    switch (tipoDatos) {
      case "CARPETAS": {
        return tipoGrafico === Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS
          ? this.CTXGraficoLineas.nativeElement.getContext('2d')
          : this.CTXGraficoCircular.nativeElement.getContext('2d');
      }
      case "CARPETA": {
        return tipoGrafico === Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS
          ? this.CTXGraficoLineasCarpeta.nativeElement.getContext('2d')
          : this.CTXGraficoCircularCarpeta.nativeElement.getContext('2d');
      }
      case "PRUEBA": {
        return tipoGrafico === Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS
          ? this.CTXGraficoLineasPrueba.nativeElement.getContext('2d')
          : this.CTXGraficoCircularPrueba.nativeElement.getContext('2d');
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

  private generarColorAleatorio():string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i+=1) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  private getEtiquetasGrafico(tipoGrafico: any, tipoDatos: string): string[] {
    if(tipoGrafico === Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS) {
      return this.busquedaAnterior.modoAnual ? Globales.LISTADO_MESES : this.getListadoDiasDelMesSeleccionado()
    }
    let datosGrafico = null;
    switch (tipoDatos) {
      case "CARPETAS": datosGrafico = this.datosGraficoTotal; break;
      case "CARPETA": datosGrafico = this.datosGraficoFiltradoPorCarpeta; break;
      case "PRUEBA": datosGrafico = this.datosGraficoFiltradoPorPrueba; break;
    }
    return datosGrafico?.map((datosCentro: any) => datosCentro.nombreCentro);
  }

  private generarDatosGraficoGrandeCentros() {
    return [
      {
        nombreCentro: 'Hospital de la Princesa',
        citas: this.busquedaAnterior.modoAnual ? [346, 337, 177, 428, 97, 240, 376, 40, 295, 118, 49, 474] : [4, 8, 3, 16, 5, 0, 0, 3, 10, 11, 9, 7, 0, 0, 4, 5, 3, 6, 16, 0, 0, 5, 3, 4, 3, 17, 0, 0, 1, 4, 14],
        idCentro: 1,
      },
      {
        nombreCentro: 'Hospital Universitario del Henares',
        citas: this.busquedaAnterior.modoAnual ? [525, 489, 189, 125, 264, 513, 177, 100, 368, 545, 415, 465] : [8, 6, 8, 17, 15, 0, 0, 11, 3, 20, 15, 13, 0, 0, 4, 9, 13, 1, 14, 0, 0, 20, 16, 20, 9, 20, 0, 0, 16, 5, 6],
        idCentro: 2,
      },
      {
        nombreCentro: 'Hospital Universitario de Getafe',
        citas: this.busquedaAnterior.modoAnual ? [224, 275, 167, 304, 126, 322, 254, 268, 207, 156, 155, 38] : [12, 12, 4, 11, 6, 0, 0, 5, 2, 7, 2, 1, 0, 0, 13, 8, 1, 12, 14, 0, 0, 1, 1, 3, 5, 13, 0, 0, 1, 9, 7],
        idCentro: 3,
      },
      {
        nombreCentro: 'Hospital Universitario Infanta Elena',
        citas: this.busquedaAnterior.modoAnual ? [183, 192, 295, 65, 280, 96, 243, 129, 353, 115, 127, 156] : [3, 2, 14, 6, 8, 0, 0, 2, 6, 5, 13, 7, 0, 0, 5, 5, 8, 14, 10, 0, 0, 5, 5, 5, 11, 11, 0, 0, 4, 2, 9],
        idCentro: 4,
      },
    ];
  }
  
  private generarDatosGraficoGrandeDeCarpeta(idCarpeta: number) {
    return idCarpeta == 2 ? [
      {
        nombreCentro: 'Hospital de la Princesa',
        citas: this.busquedaAnterior.modoAnual ? [76, 144, 206, 161, 168, 170, 82, 70, 268, 162, 106, 200] : [4, 2, 5, 10, 2, 0, 0, 7, 6, 9, 5, 6, 0, 0, 3, 9, 7, 7, 3, 0, 0, 1, 3, 6, 5, 8, 0, 0, 6, 1, 2],
        idCentro: 1,
      },
      {
        nombreCentro: 'Hospital Universitario del Henares',
        citas: this.busquedaAnterior.modoAnual ? [136, 37, 178, 135, 32, 334, 323, 298, 247, 341, 264, 34] : [3, 13, 13, 2, 3, 0, 0, 13, 8, 10, 4, 12, 0, 0, 9, 6, 1, 10, 7, 0, 0, 11, 10, 1, 13, 11, 0, 0, 10, 5, 12],
        idCentro: 2,
      },
      {
        nombreCentro: 'Hospital Universitario de Getafe',
        citas: this.busquedaAnterior.modoAnual ? [74, 98, 84, 166, 93, 94, 32, 172, 145, 63, 92, 45] : [4, 2, 2, 1, 5, 0, 0, 5, 1, 6, 5, 2, 0, 0, 4, 1, 4, 2, 1, 0, 0, 1, 2, 6, 6, 2, 0, 0, 5, 7, 2],
        idCentro: 3,
      },
      {
        nombreCentro: 'Hospital Universitario Infanta Elena',
        citas: this.busquedaAnterior.modoAnual ? [71, 73, 109, 126, 172, 41, 108, 80, 134, 56, 42, 125] : [4, 3, 6, 4, 5, 0, 0, 7, 1, 5, 3, 4, 0, 0, 4, 3, 7, 6, 4, 0, 0, 1, 1, 6, 2, 4, 0, 0, 6, 3, 2],
        idCentro: 4,
      },
    ] : [
      {
        nombreCentro: 'Hospital de la Princesa',
        citas: this.busquedaAnterior.modoAnual ? [40, 54, 73, 76, 19, 26, 66, 4, 14, 110, 35, 106] : [4, 4, 2, 0, 3, 0, 0, 3, 0, 2, 0, 3, 0, 0, 2, 2, 2, 3, 4, 0, 0, 1, 0, 2, 1, 1, 0, 0, 4, 0, 2],
        idCentro: 1,
      },
      {
        nombreCentro: 'Hospital Universitario del Henares',
        citas: this.busquedaAnterior.modoAnual ? [104, 95, 15, 64, 58, 88, 115, 58, 30, 32, 18, 77] : [4, 2, 1, 4, 0, 0, 0, 1, 3, 1, 0, 2, 0, 0, 4, 1, 1, 2, 4, 0, 0, 4, 2, 4, 0, 3, 0, 0, 4, 0, 2],
        idCentro: 2,
      },
      {
        nombreCentro: 'Hospital Universitario de Getafe',
        citas: this.busquedaAnterior.modoAnual ? [100, 96, 104, 73, 72, 51, 28, 96, 67, 65, 50, 105] : [3, 1, 1, 2, 2, 0, 0, 4, 2, 0, 3, 3, 0, 0, 1, 3, 1, 2, 1, 0, 0, 0, 1, 2, 2, 3, 0, 0, 1, 2, 4],
        idCentro: 3,
      },
      {
        nombreCentro: 'Hospital Universitario Infanta Elena',
        citas: this.busquedaAnterior.modoAnual ? [51, 9, 112, 55, 20, 94, 26, 60, 2, 29, 84, 54] : [2, 3, 0, 1, 2, 0, 0, 3, 2, 0, 3, 0, 0, 0, 3, 1, 4, 0, 1, 0, 0, 2, 3, 2, 4, 4, 0, 0, 4, 4, 3],
        idCentro: 4,
      },
    ];
  }

  private generarDatosGraficoGrandeDePrueba(idPrueba: number) {
    return idPrueba == 3 || idPrueba == 4 || idPrueba == 5 ? [
      {
        nombreCentro: 'Hospital de la Princesa',
        citas: this.busquedaAnterior.modoAnual ? [52, 18, 33, 27, 69, 8, 58, 26, 23, 52, 56, 13] : [2, 1, 2, 1, 3, 0, 0, 3, 2, 1, 1, 2, 0, 0, 3, 0, 2, 1, 2, 0, 0, 1, 2, 3, 1, 3, 0, 0, 3, 3, 1],
        idCentro: 1,
      },
      {
        nombreCentro: 'Hospital Universitario del Henares',
        citas: this.busquedaAnterior.modoAnual ? [66, 103, 9, 39, 47, 86, 19, 19, 19, 11, 86, 96] : [4, 4, 3, 4, 4, 0, 0, 2, 3, 0, 2, 1, 0, 0, 3, 3, 2, 3, 0, 0, 0, 4, 2, 3, 2, 3, 0, 0, 0, 2, 4],
        idCentro: 2,
      },
      {
        nombreCentro: 'Hospital Universitario de Getafe',
        citas: this.busquedaAnterior.modoAnual ? [37, 31, 14, 2, 15, 40, 38, 24, 50, 1, 39, 47] : [0, 0, 1, 2, 2, 0, 0, 1, 0, 2, 1, 1, 0, 0, 1, 0, 2, 1, 1, 0, 0, 1, 2, 0, 2, 1, 0, 0, 0, 1, 1],
        idCentro: 3,
      },
      {
        nombreCentro: 'Hospital Universitario Infanta Elena',
        citas: this.busquedaAnterior.modoAnual ? [52, 1, 45, 5, 42, 22, 42, 48, 27, 4, 37, 5] : [2, 2, 2, 2, 0, 0, 0, 2, 0, 2, 0, 2, 0, 0, 1, 1, 2, 1, 2, 0, 0, 1, 2, 0, 2, 0, 0, 0, 2, 0, 1],
        idCentro: 4,
      },
    ] : [
      {
        nombreCentro: 'Hospital de la Princesa',
        citas: this.busquedaAnterior.modoAnual ? [34, 33, 31, 30, 39, 43, 56, 36, 61, 20, 29, 14] : [2, 2, 0, 0, 2, 0, 0, 1, 0, 2, 1, 2, 0, 0, 1, 1, 0, 1, 2, 0, 0, 2, 0, 1, 1, 1, 0, 0, 0, 2, 1],
        idCentro: 1,
      },
      {
        nombreCentro: 'Hospital Universitario del Henares',
        citas: this.busquedaAnterior.modoAnual ? [52, 33, 5, 25, 20, 23, 7, 50, 28, 44, 31, 7] : [2, 1, 0, 1, 0, 0, 0, 2, 1, 0, 1, 1, 0, 0, 1, 2, 0, 1, 1, 0, 0, 0, 2, 0, 2, 1, 0, 0, 2, 2, 1],
        idCentro: 2,
      },
      {
        nombreCentro: 'Hospital Universitario de Getafe',
        citas: this.busquedaAnterior.modoAnual ? [47, 15, 30, 26, 44, 22, 34, 14, 49, 18, 26, 44] : [1, 1, 0, 0, 2, 0, 0, 2, 1, 0, 2, 1, 0, 0, 1, 0, 1, 2, 2, 0, 0, 2, 2, 0, 0, 1, 0, 0, 2, 2, 0],
        idCentro: 3,
      },
      {
        nombreCentro: 'Hospital Universitario Infanta Elena',
        citas: this.busquedaAnterior.modoAnual ? [29, 24, 56, 11, 6, 49, 11, 25, 20, 29, 38, 25] : [1, 1, 0, 2, 1, 0, 0, 2, 1, 2, 0, 2, 0, 0, 2, 2, 0, 1, 2, 0, 0, 2, 1, 0, 0, 1, 0, 0, 1, 2, 0],
        idCentro: 4,
      },
    ];
  }

  ngOnInit(): void {}

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

  getRandomInt(max: number) {
    return Math.floor(Math.random() * max);
  }

  public pedirEstadisticasCentros() {
    this.formulario.markAllAsTouched();
    if (this.formulario.valid) {
      if (this.datosGraficoTotal) {
        this.graficoLineasTotal?.destroy();
        this.graficoCircularTotal?.destroy();
        this.graficoLineasPrueba?.destroy();
        this.graficoCircularPrueba?.destroy();
      }
      this.datosGraficoFiltradoPorCarpeta = null;
      this.datosGraficoFiltradoPorPrueba = null;
      this.selectorCarpeta.setValue('');
      this.selectorCarpeta.markAsPending();
      this.selectorPrueba.setValue('');
      this.selectorPrueba.markAsPending();
      this.busquedaAnterior = this.formulario.value;

      // this.datosGraficoTotal = [
      //   {idCentro: 1, nombreCentro: "Hospital la Princesa", citas: this.busquedaAnterior.modoAnual ? Array.from(Array(12).keys()).map(()=>this.getRandomInt(20)) : Array.from(Array(31).keys()).map(()=>this.getRandomInt(30))},
      //   {idCentro: 2, nombreCentro: "Hospital de la Paz", citas: this.busquedaAnterior.modoAnual ? Array.from(Array(12).keys()).map(()=>this.getRandomInt(20)) : Array.from(Array(31).keys()).map(()=>this.getRandomInt(20))},
      // ];
      // this.iniciarListadoColoresCentros();
      // this.graficoLineas = this.inicializarGraficoLineas(false);
      // this.graficoCircular = this.inicializarGraficoCircular(false);
      // console.log(this.datosGraficoTotal)

      this.servicioEstadisticasComunidad.getDatosGraficosEstadisticasCentrosComunidad(
        this.formulario.get('modoAnual').value ? this.busquedaAnterior.anno : this.busquedaAnterior.fecha.format(Globales.FORMATO_FECHA_MES),
        this.formulario.get('modoAnual').value,
      ).then((datosGrafico: any) => {
        this.datosGraficoTotal = datosGrafico;
        this.datosGraficoTotal = this.generarDatosGraficoGrandeCentros();
        this.iniciarListadoColoresCentros();
        this.graficoLineasTotal = this.inicializarGraficoLineas("CARPETAS");
        this.graficoCircularTotal = this.inicializarGraficoCircular("CARPETAS");
        console.log(this.datosGraficoTotal)
      });
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO)
    }
  }

  public pedirEstadisticasPorCarpeta() {
    this.selectorCarpeta.markAllAsTouched();
    if (this.selectorCarpeta.valid) {
      if (this.datosGraficoFiltradoPorCarpeta) {
        this.graficoLineasCarpeta?.destroy();
        this.graficoCircularCarpeta?.destroy();
      }
      this.ultimaCarpetaFiltrada = this.selectorCarpeta.value;
      // this.datosGraficoFiltradoPorPrueba = [
      //   {idCentro: 1, nombreCentro: "Hospital la Princesa", citas: this.busquedaAnterior.modoAnual ? Array.from(Array(12).keys()).map(()=>this.getRandomInt(20)) : Array.from(Array(31).keys()).map(()=>this.getRandomInt(30))},
      //   {idCentro: 2, nombreCentro: "Hospital de la Paz", citas: this.busquedaAnterior.modoAnual ? Array.from(Array(12).keys()).map(()=>this.getRandomInt(20)) : Array.from(Array(31).keys()).map(()=>this.getRandomInt(20))},
      // ];
      // this.graficoLineas = this.inicializarGraficoLineas(true);
      // this.graficoCircular = this.inicializarGraficoCircular(true);
      this.servicioEstadisticasComunidad.getDatosGraficosEstadisticasCarpetaComunidad(
        this.selectorCarpeta.value.id,
        this.busquedaAnterior.modoAnual ? this.busquedaAnterior.anno : this.busquedaAnterior.fecha.format(Globales.FORMATO_FECHA_MES),
        this.busquedaAnterior.modoAnual,
      ).then((datosGrafico: any) => {
        this.datosGraficoFiltradoPorCarpeta = datosGrafico;
        this.datosGraficoFiltradoPorCarpeta = this.generarDatosGraficoGrandeDeCarpeta(this.selectorCarpeta.value.id);
        this.graficoLineasCarpeta = this.inicializarGraficoLineas("CARPETA");
        this.graficoCircularCarpeta = this.inicializarGraficoCircular("CARPETA");
      });
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO)
    }
  }
  
  public pedirEstadisticasPorPrueba() {
    this.selectorPrueba.markAllAsTouched();
    if (this.selectorPrueba.valid) {
      if (this.datosGraficoFiltradoPorPrueba) {
        this.graficoLineasPrueba?.destroy();
        this.graficoCircularPrueba?.destroy();
      }
      this.ultimaPruebaFiltrada = this.selectorPrueba.value;
      // this.datosGraficoFiltradoPorPrueba = [
      //   {idCentro: 1, nombreCentro: "Hospital la Princesa", citas: this.busquedaAnterior.modoAnual ? Array.from(Array(12).keys()).map(()=>this.getRandomInt(20)) : Array.from(Array(31).keys()).map(()=>this.getRandomInt(30))},
      //   {idCentro: 2, nombreCentro: "Hospital de la Paz", citas: this.busquedaAnterior.modoAnual ? Array.from(Array(12).keys()).map(()=>this.getRandomInt(20)) : Array.from(Array(31).keys()).map(()=>this.getRandomInt(20))},
      // ];
      // this.graficoLineas = this.inicializarGraficoLineas(true);
      // this.graficoCircular = this.inicializarGraficoCircular(true);
      this.servicioEstadisticasComunidad.getDatosGraficosEstadisticasPruebaComunidad(
        this.selectorPrueba.value.id,
        this.busquedaAnterior.modoAnual ? this.busquedaAnterior.anno : this.busquedaAnterior.fecha.format(Globales.FORMATO_FECHA_MES),
        this.busquedaAnterior.modoAnual,
      ).then((datosGrafico: any) => {
        this.datosGraficoFiltradoPorPrueba = datosGrafico;
        this.datosGraficoFiltradoPorPrueba = this.generarDatosGraficoGrandeDePrueba(this.selectorPrueba.value.id);
        this.graficoLineasPrueba = this.inicializarGraficoLineas("PRUEBA");
        this.graficoCircularPrueba = this.inicializarGraficoCircular("PRUEBA");
      });
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO)
    }
  }

}
