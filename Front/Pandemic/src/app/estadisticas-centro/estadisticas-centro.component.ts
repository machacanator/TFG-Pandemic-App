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
import { ServicioEstadisticasCentro } from './estadisticas-centro.service';

@Component({
  selector: 'app-estadisticas-centro',
  templateUrl: './estadisticas-centro.component.html',
  styleUrls: ['./estadisticas-centro.component.scss']
})
export class EstadisticasCentro implements OnInit {
  @ViewChild('graficoLineasCarpetas') CTXGraficoLineasTodasCarpetas: ElementRef;
  @ViewChild('graficoCircularCarpetas') CTXGraficoCircularTodasCarpetas: ElementRef;
  @ViewChild('graficoLineasCarpeta') CTXGraficoLineasCarpetaSeleccionada: ElementRef;
  @ViewChild('graficoCircularCarpeta') CTXGraficoCircularCarpetaSeleccionada: ElementRef;

  cabeceraCalendario = CabeceraCalendarioCitas;
  graficoLineasCarpetas: Chart = null;
  graficoCircularCarpetas: Chart = null;
  graficoLineasCarpeta: Chart = null;
  graficoCircularCarpeta: Chart = null;
  listadoColoresPruebas: any[];
  listadoColoresCarpetas: any[];
  ultimaBusquedaDeTodasCarpetas: any;
  ultimaBusquedaDeCarpeta: any;
  listadoAnnosDisponibles: string[];
  listaCarpetasDePruebasCentro: any[];
  nombreCentro: string = "";
  formulario: FormGroup<any>;
  datosGraficosTodasCarpetas: any[] = null;
  datosGraficosCarpetaSeleccionada: any[] = null;
  selectorCarpeta: FormControl;
  filtroFechas = (fecha: moment.Moment) => (new Date().getFullYear() - fecha?.year()) < 5;

  constructor (
    private servicioPopUp: ServicioPopUp,
    private servicioAplicacion: ServicioAplicacion,
    private servicioEstadisticasCentro: ServicioEstadisticasCentro,
  ) {
    this.pedirNombreCentroDeAdmin();
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

  private pedirNombreCentroDeAdmin() {
    this.servicioEstadisticasCentro.getNombreCentro()
      .then((nombre: any) => {this.nombreCentro = nombre;})
      .catch(() => {this.nombreCentro = ""; this.servicioPopUp.open(Globales.TIPOS_POPUP.ERROR, Errores.MENSAJES.ERROR_NOMBRE_CENTRO);})
  }

  private iniciarFormulario() {
    let fechaActual: moment.Moment = moment();
    fechaActual.format(Globales.FORMATO_FECHA_NACIONAL);
    this.formulario = new FormGroup({
      fecha: new FormControl({value: fechaActual, disabled: false}, [Validators.required, validadorFecha()]),
      anno: new FormControl({value: this.listadoAnnosDisponibles[0], disabled: false}, [Validators.required]), 
      modoAnual: new FormControl({value: false, disabled: false}, []),
    });
    this.selectorCarpeta = new FormControl({value: '', disabled: false}, [Validators.required])
  }

  private iniciarListadoColoresPruebas() {
   this.listadoColoresPruebas = this.servicioAplicacion.listadoPruebas.map((prueba: any) => ({id: prueba.id, color: this.generarColorAleatorio()}));
  }

  private iniciarListadoColoresCarpetas() {
    this.listadoColoresCarpetas = this.servicioAplicacion.listadoCarpetasPruebas.map((carpeta: any) => ({id: carpeta.id, color: this.generarColorAleatorio()}));
  }

  private inicializarGraficoLineas(perteneceAFiltroCarpeta: boolean) {
    const listaDeDatos = perteneceAFiltroCarpeta ? this.datosGraficosCarpetaSeleccionada : this.datosGraficosTodasCarpetas;
    const opcionesGrafico: any = {
      scales: {
        x: {
          title: {
            display: true,
            text: this.ultimaBusquedaDeTodasCarpetas.modoAnual
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
      labels: this.getEtiquetasGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS, null),
      datasets: listaDeDatos?.map(
        (prueba) => {
          const color = perteneceAFiltroCarpeta
            ? this.listadoColoresPruebas.find((infoPrueba)=> infoPrueba.id === prueba.idPrueba).color
            : this.listadoColoresCarpetas.find((infoCarpeta)=> infoCarpeta.id === prueba.idPrueba).color;
          return {
            data: prueba.citas,
            label: prueba.nombrePrueba,
            // backgroundColor: color,
            backgroundColor: color,
            borderColor: color,
          };
        },
      ),
    };
    console.log(datosGrafico)
    return new Chart(this.getCTXDeGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS, perteneceAFiltroCarpeta), {
      type: this.getConfiguracionTipoGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS),
      options: opcionesGrafico,
      data: datosGrafico,
    });
  }

  private inicializarGraficoCircular(perteneceAFiltroCarpeta: boolean) {
    const listaDeDatos = perteneceAFiltroCarpeta ? this.datosGraficosCarpetaSeleccionada : this.datosGraficosTodasCarpetas;
    
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
      labels: this.getEtiquetasGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR, perteneceAFiltroCarpeta),
      datasets: [
        {
          data: listaDeDatos?.map((datosPrueba: any) => datosPrueba.citas.reduce((totalCitas: number, numeroDeCitas: number) => totalCitas + numeroDeCitas, 0)),
          label: listaDeDatos?.map((datosPrueba: any) => datosPrueba.nombrePrueba),
          backgroundColor: perteneceAFiltroCarpeta
          ? listaDeDatos?.map((prueba: any) => this.listadoColoresPruebas.find((infoPrueba)=> infoPrueba.id === prueba.idPrueba).color)
          : listaDeDatos?.map((carpeta: any) => this.listadoColoresCarpetas.find((infoCarpeta)=> infoCarpeta.id === carpeta.idPrueba).color),
        },
      ]
    };

    return  new Chart(this.getCTXDeGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR, perteneceAFiltroCarpeta), {
      type: this.getConfiguracionTipoGrafico(Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR),
      options: opcionesGrafico,
      data: datosGrafico,
    });
  }

  private getMaximoNumeroDeCitas(datos: any): number {
    let a: number[] = [];
    let maximoNumeroDeCitas = 0;
    datos.forEach((prueba: any) => {
      prueba.citas.forEach((citasPrueba: number) => {
        if(citasPrueba > maximoNumeroDeCitas) maximoNumeroDeCitas = citasPrueba;
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

  private getCTXDeGrafico(tipoGrafico: number, perteneceAFiltroCarpeta: boolean) {
    switch (tipoGrafico) {
      case Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS: {
        return perteneceAFiltroCarpeta
          ? this.CTXGraficoLineasCarpetaSeleccionada.nativeElement.getContext('2d')
          : this.CTXGraficoLineasTodasCarpetas.nativeElement.getContext('2d');
      }
      case Globales.TIPOS_GRAFICOS.GRAFICO_CIRCULAR: {
        return perteneceAFiltroCarpeta
          ? this.CTXGraficoCircularCarpetaSeleccionada.nativeElement.getContext('2d')
          : this.CTXGraficoCircularTodasCarpetas.nativeElement.getContext('2d');
      }
      default: return undefined;
    }
  }

  private getListadoDiasDelMesSeleccionado(): string[] {
    const diasDelMesSeleccionado:number = this.ultimaBusquedaDeTodasCarpetas.fecha.daysInMonth();
    let listadoDias: any[] = [];
    for(let dia = 1; dia <= diasDelMesSeleccionado; dia++) {
      listadoDias.push(dia.toString());
    }
    return listadoDias;
  }

  private destruirTodosLosGraficos() {
    this.graficoLineasCarpetas?.destroy();
    this.graficoCircularCarpetas?.destroy();
    this.destruirGraficosDeCarpeta();
  }

  private destruirGraficosDeCarpeta() {
    this.graficoLineasCarpeta?.destroy();
    this.graficoCircularCarpeta?.destroy();
  }

  private generarColorAleatorio():string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i+=1) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  private getEtiquetasGrafico(tipoGrafico: any, perteneceAFiltroCarpeta: boolean): string[] {
    if(tipoGrafico === Globales.TIPOS_GRAFICOS.GRAFICO_LINEAS) {
      return this.ultimaBusquedaDeTodasCarpetas.modoAnual ? Globales.LISTADO_MESES : this.getListadoDiasDelMesSeleccionado()
    }
    const datosGrafico = perteneceAFiltroCarpeta ?  this.datosGraficosCarpetaSeleccionada : this.datosGraficosTodasCarpetas; 
    return datosGrafico.map((datosPrueba: any) => datosPrueba.nombrePrueba);
  }

  private resetearFiltroCarpeta() {
    this.ultimaBusquedaDeCarpeta = null;
    this.selectorCarpeta.setValue('');
    this.datosGraficosCarpetaSeleccionada = null;
  }

  private generarDatosGraficoGrandeTodasLasCarpetas() {
    return [
      {
        nombrePrueba: 'Neumococo',
        citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [68, 14, 11, 70, 52, 79, 2, 8, 34, 1, 29, 29] : [2, 3, 0, 1, 3, 0, 0, 1, 1, 2, 0, 1, 0, 0, 2, 2, 0, 1, 1, 0, 0, 3, 0, 0, 3, 1, 0, 0, 3, 0, 2],
        idPrueba: 1
      },
      {
        nombrePrueba: 'Covid',
        citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [279, 234, 100, 109, 140, 232, 146, 66, 242, 102, 105, 273] : [2, 10, 9, 1, 4, 0, 0, 7, 6, 3, 6, 6, 0, 0, 10, 2, 5, 5, 2, 0, 0, 2, 4, 9, 5, 10, 0, 0, 2, 4, 5],
        idPrueba: 2
      },
      {
        nombrePrueba: 'Gripe',
        citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [108, 102, 76, 26, 59, 26, 74, 0, 107, 78, 71, 103] : [4, 1, 1, 2, 1, 0, 0, 1, 2, 0, 3, 3, 0, 0, 3, 4, 3, 4, 3, 0, 0, 1, 3, 3, 1, 4, 0, 0, 1, 1, 3],
        idPrueba: 3
      },
    ];
  }

  private generarDatosGraficoGrandeDeCarpeta(idCarpeta: number) {
    switch(idCarpeta) {
      case 1: return [
        {
          nombrePrueba: 'Neumococo PCV15',
          citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [32, 30, 14, 25, 40, 19, 42, 18, 18, 4, 7, 24] : [2, 0, 0, 1, 1, 0, 0, 0, 2, 0, 0, 2, 0, 0, 1, 2, 2, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 2, 0],
          idPrueba: 1
        },
        {
          nombrePrueba: 'Neumococo PPSV23',
          citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [28, 10, 23, 36, 19, 18, 21, 28, 20, 45, 44, 13] : [1, 0, 0, 2, 2, 0, 0, 2, 1, 2, 0, 2, 0, 0, 1, 2, 1, 0, 1, 0, 0, 0, 1, 1, 2, 2, 0, 0, 0, 1, 2],
          idPrueba: 2
        },
      ];
      case 2: return [
        {
          nombrePrueba: 'Covid Vacuna 1',
          citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [68, 94, 61, 99, 43, 42, 70, 93, 62, 64, 35, 95] : [1, 0, 1, 1, 3, 0, 0, 3, 3, 1, 2, 2, 0, 0, 1, 2, 2, 3, 1, 0, 0, 1, 3, 1, 3, 3, 0, 0, 1, 3, 3],
          idPrueba: 3
        },
        {
          nombrePrueba: 'Covid Vacuna 2',
          citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [53, 64, 68, 64, 87, 47, 86, 43, 69, 89, 85, 63] : [2, 1, 2, 1, 3, 0, 0, 0, 1, 1, 3, 0, 0, 0, 1, 2, 0, 1, 1, 0, 0, 3, 3, 2, 2, 2, 0, 0, 0, 3, 3],
          idPrueba: 4
        },
        {
          nombrePrueba: 'Covid Vacuna 3',
          citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [32, 87, 94, 71, 52, 48, 67, 40, 87, 57, 36, 72] : [3, 1, 2, 0, 2, 0, 0, 1, 2, 2, 3, 3, 0, 0, 0, 1, 1, 2, 3, 0, 0, 0, 2, 1, 2, 3, 0, 0, 0, 2, 2],
          idPrueba: 5
        },
      ]; 
      case 3: return [
        {
          nombrePrueba: 'Gripe Vacuna 1',
          citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [15, 6, 55, 52, 5, 23, 22, 28, 23, 22, 3, 39] : [0, 0, 2, 1, 1, 0, 0, 0, 2, 1, 1, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 2, 0, 2, 1, 2, 0, 0, 1, 2, 2],
          idPrueba: 6
        },
        {
          nombrePrueba: 'Gripe Vacuna 2',
          citas: this.ultimaBusquedaDeTodasCarpetas.modoAnual ? [57, 60, 5, 61, 3, 21, 9, 3, 0, 5, 16, 10] : [2, 1, 0, 2, 1, 0, 0, 2, 1, 1, 1, 2, 0, 0, 2, 0, 1, 2, 2, 0, 0, 1, 2, 0, 1, 2, 0, 0, 1, 2, 0],
          idPrueba: 7
        },
      ];
    }
    return [];
  }

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    // this.CTXGraficosCirculares.changes.subscribe((changes) => {
    //   if(this.datosGraficos) {
    //     this.graficoCircular = this.inicializarGraficoCircular();
    //   }
    // });
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

  getRandomInt(max: number) {
    return Math.floor(Math.random() * max);
  }

  public pedirEstadisticasTodasCarpetas() {
    this.formulario.markAllAsTouched();
    if (this.formulario.valid) {
      if (this.datosGraficosTodasCarpetas) {
        this.destruirTodosLosGraficos();
      }
      this.ultimaBusquedaDeTodasCarpetas = this.formulario.value;
      this.resetearFiltroCarpeta();
      this.servicioEstadisticasCentro.getDatosGraficosEstadisticasTodasCarpetasCentroAdministrador(
        this.formulario.get('modoAnual').value ? this.ultimaBusquedaDeTodasCarpetas.anno : this.ultimaBusquedaDeTodasCarpetas.fecha.format(Globales.FORMATO_FECHA_MES),
        this.formulario.get('modoAnual').value,
      ).then((datosGrafico: any) => {
        this.datosGraficosTodasCarpetas = datosGrafico;
        this.datosGraficosTodasCarpetas = this.generarDatosGraficoGrandeTodasLasCarpetas();
        this.listaCarpetasDePruebasCentro = datosGrafico.map((carpeta: any) => ({id: carpeta.idPrueba, nombre: carpeta.nombrePrueba}));
        this.iniciarListadoColoresCarpetas();
        this.graficoLineasCarpetas = this.inicializarGraficoLineas(false);
        this.graficoCircularCarpetas = this.inicializarGraficoCircular(false);
        console.log(this.datosGraficosTodasCarpetas)
      });
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO)
    }
  }

  public pedirEstadisticasDeCarpeta() {
    this.formulario.markAllAsTouched();
    if (this.selectorCarpeta.valid) {
      if (this.datosGraficosCarpetaSeleccionada) {
        this.destruirGraficosDeCarpeta();
      }
      this.ultimaBusquedaDeCarpeta = this.selectorCarpeta.value;
      this.servicioEstadisticasCentro.getDatosGraficosEstadisticasDeCarpetaCentroAdministrador(
        this.selectorCarpeta?.value?.id,
        this.ultimaBusquedaDeTodasCarpetas.modoAnual ? this.ultimaBusquedaDeTodasCarpetas.anno : this.ultimaBusquedaDeTodasCarpetas.fecha.format(Globales.FORMATO_FECHA_MES),
        this.ultimaBusquedaDeTodasCarpetas.modoAnual,
      ).then((datosGrafico: any) => {
        this.datosGraficosCarpetaSeleccionada = datosGrafico;
        this.datosGraficosCarpetaSeleccionada = this.generarDatosGraficoGrandeDeCarpeta(this.selectorCarpeta?.value?.id);
        this.iniciarListadoColoresPruebas();
        this.graficoLineasCarpeta = this.inicializarGraficoLineas(true);
        this.graficoCircularCarpeta = this.inicializarGraficoCircular(true);
        console.log(this.datosGraficosCarpetaSeleccionada)
      });
    } else {
      this.servicioPopUp.open(Globales.TIPOS_POPUP.AVISO, Errores.MENSAJES.FORMULARIO_NO_VALIDO)
    }
  }

}
