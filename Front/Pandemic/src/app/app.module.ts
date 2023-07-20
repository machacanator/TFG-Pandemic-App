import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter } from '@angular/material-moment-adapter';
import { MatButtonModule } from '@angular/material/button';
import { DateAdapter, MatNativeDateModule, MatRippleModule, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorIntl, MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import 'moment/locale/es';
import { BarraInferior } from './barra-inferior/barra-inferior.component';
import { BarraSuperior } from './barra-superior/barra-superior.component';
import { CabeceraCalendarioCitas } from './cabecera-calendario-citas/cabecera-calendario-citas.component';
import { CitasPendientes } from './citas-pendientes/citas-pendientes.component';
import { ConfirmacionCancelarCita } from './citas-pendientes/confirmacion-cancelar-cita/confirmacion-cancelar-cita.component';
import { HistoricoCitas } from './historico-citas/historico-citas.component';
import { MenuDesplegableComponent } from './menu-desplegable/menu-desplegable.component';
import { MenuPrincipal } from './menu-principal/menu-principal.component';
import { Notificaciones } from './notificaciones/notificaciones.component';
import { NuevaCita } from './nueva-cita/nueva-cita.component';
import { Paginador } from './paginador/paginador.component';
import { PopUp } from './popup/componente/pop-up.component';
import { ServicioAplicacion } from './servicio-app.service';
import { InicioSesion } from './sesion/inicio-sesion/inicio-sesion.component';
import InterceptorTokens from './tokens/servicio-tokens.service';
import { DiarioCitas } from './diario-citas/diario-citas.component';
import { CdkColumnDef, CdkTableModule } from '@angular/cdk/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { GestionarCita } from './gestionar-cita/gestionar-cita.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips';
import { MatRadioModule } from '@angular/material/radio';
import { EstadisticasEnfermero } from './estadisticas-enfermero/estadisticas-enfermero.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { AdministrarPlantillaCentro } from './administrar-plantilla-centro/administrar-plantilla-centro.component';
import { AdministrarPruebasCentro } from './administrar-pruebas-centro/administrar-pruebas-centro.component';
import { ConfirmacionEliminarEmpleado } from './administrar-plantilla-centro/confirmacion-eliminar-empleado/confirmacion-eliminar-empleado.component';
import { AnnadirEmpleado } from './annadir-empleado/annadir-empleado.component';
import { MatListModule } from '@angular/material/list';
import { ConfirmacionEliminarPruebaCentro } from './administrar-pruebas-centro/confirmacion-eliminar-prueba-centro/confirmacion-eliminar-prueba-centro.component';
import { AnnadirPruebaCentro } from './annadir-prueba-centro/annadir-prueba-centro.component';
import { HammerModule } from '@angular/platform-browser';
import { 
	IgxTimePickerModule,
	IgxInputGroupModule,
	IgxIconModule
 } from "igniteui-angular";
import { EstadisticasCentro } from './estadisticas-centro/estadisticas-centro.component';
import { EstadisticasEmpleadosCentro } from './estadisticas-empleados-centro/estadisticas-empleados-centro.component';
import { AdministrarPruebasComunidad } from './administrar-pruebas-comunidad/administrar-pruebas-comunidad.component';
import { AdministrarCentrosComunidad } from './administrar-centros-comunidad/administrar-centros-comunidad.component';
import { EstadisticasCentrosComunidad } from './estadisticas-centros-comunidad/estadisticas-centros-comunidad.component';
import { EstadisticasComunidad } from './estadisticas-comunidad/estadisticas-comunidad.component';
import { ConfirmacionEliminarCentro } from './administrar-centros-comunidad/confirmacion-eliminar-centro/confirmacion-eliminar-centro.component';
import { AnnadirCentro } from './annadir-centro-comunidad/annadir-centro-comunidad.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MiCentro } from './mi-centro/mi-centro.component';
import { InformacionPruebas } from './informacion-pruebas/informacion-pruebas.component';
import { Carousel } from './carousel/carousel.component';
import { AdministrarCarpetasPruebasComunidad } from './administrar-carpetas-pruebas-comunidad/administrar-carpetas-pruebas-comunidad.component';
import { AnnadirPruebaComunidad } from './annadir-prueba-comunidad/annadir-prueba-comunidad.component';
import { ConfirmacionEliminarCarpetaDePruebas } from './administrar-carpetas-pruebas-comunidad/confirmacion-eliminar-carpeta-de-pruebas/confirmacion-eliminar-carpeta-de-pruebas.component';
import { ConfirmacionEliminarPrueba } from './administrar-pruebas-comunidad/confirmacion-eliminar-prueba/confirmacion-eliminar-prueba.component';
import { NuevaCarpetaDePruebas } from './administrar-carpetas-pruebas-comunidad/nueva-carpeta-de-pruebas/nueva-carpeta-de-pruebas.component';

export function appInit(servicioAplicacion: ServicioAplicacion) { return () => servicioAplicacion.cargarDatos();}

export const DATE_FORMAT = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@NgModule({
  declarations: [
    AppComponent,
    InicioSesion,
    PopUp,
    MenuPrincipal,
    BarraSuperior,
    BarraInferior,
    MenuDesplegableComponent,
    NuevaCita,
    CitasPendientes,
    HistoricoCitas,
    Notificaciones,
    CabeceraCalendarioCitas,
    ConfirmacionCancelarCita,
    DiarioCitas,
    GestionarCita,
    EstadisticasEnfermero,
    AdministrarPlantillaCentro,
    AdministrarPruebasCentro,
    ConfirmacionEliminarEmpleado,
    AnnadirEmpleado,
    ConfirmacionEliminarPruebaCentro,
    AnnadirPruebaCentro,
    EstadisticasCentro,
    EstadisticasEmpleadosCentro,
    AdministrarPruebasComunidad,
    AdministrarCentrosComunidad,
    EstadisticasCentrosComunidad,
    EstadisticasComunidad,
    ConfirmacionEliminarCentro,
    AnnadirCentro,
    MiCentro,
    InformacionPruebas,
    Carousel,
    AdministrarCarpetasPruebasComunidad,
    AnnadirPruebaComunidad,
    ConfirmacionEliminarCarpetaDePruebas,
    ConfirmacionEliminarPrueba,
    NuevaCarpetaDePruebas,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatIconModule,
    MatToolbarModule,
    FontAwesomeModule,
    MatTableModule,
    MatPaginatorModule,
    MatRippleModule,
    MatStepperModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSortModule,
    MatDialogModule,
    MatTooltipModule,
    MatExpansionModule,
    MatChipsModule,
    MatRadioModule,
    MatCheckboxModule,
    MatListModule,
    FormsModule,
    IgxTimePickerModule,
    IgxInputGroupModule,
    IgxIconModule,
    HammerModule,
    MatGridListModule,
    MatExpansionModule,
  ],
  providers: [
    ServicioAplicacion,
    MatDatepickerModule,
    CdkColumnDef,
    CdkTableModule,
    { provide: MAT_DATE_LOCALE, useValue: 'es-ES' },
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
    },
    { provide: MAT_DATE_FORMATS, useValue: DATE_FORMAT },
    {
      provide: APP_INITIALIZER,
      useFactory: appInit,
      multi: true,
      deps: [ServicioAplicacion],
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorTokens,
      multi: true,
    },
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true, displayDefaultIndicatorType: false},
    },
    {
      provide: MatPaginatorIntl,
      useClass: Paginador,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
