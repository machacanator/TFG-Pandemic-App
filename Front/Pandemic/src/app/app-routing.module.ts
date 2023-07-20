import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InicioSesion } from './sesion/inicio-sesion/inicio-sesion.component';
import { ControladorPermisos } from './permisos/controlador-permisos.guard';
import { MenuPrincipal } from './menu-principal/menu-principal.component';
import { NuevaCita } from './nueva-cita/nueva-cita.component';
import { CitasPendientes } from './citas-pendientes/citas-pendientes.component';
import { HistoricoCitas } from './historico-citas/historico-citas.component';
import { Notificaciones } from './notificaciones/notificaciones.component';
import { DiarioCitas } from './diario-citas/diario-citas.component';
import { GestionarCita } from './gestionar-cita/gestionar-cita.component';
import { EstadisticasEnfermero } from './estadisticas-enfermero/estadisticas-enfermero.component';
import { AdministrarPlantillaCentro } from './administrar-plantilla-centro/administrar-plantilla-centro.component';
import { AdministrarPruebasCentro } from './administrar-pruebas-centro/administrar-pruebas-centro.component';
import { AnnadirEmpleado } from './annadir-empleado/annadir-empleado.component';
import { AnnadirPruebaCentro } from './annadir-prueba-centro/annadir-prueba-centro.component';
import { EstadisticasCentro } from './estadisticas-centro/estadisticas-centro.component';
import { EstadisticasEmpleadosCentro } from './estadisticas-empleados-centro/estadisticas-empleados-centro.component';
import { AdministrarCentrosComunidad } from './administrar-centros-comunidad/administrar-centros-comunidad.component';
import { AnnadirCentro } from './annadir-centro-comunidad/annadir-centro-comunidad.component';
import { InformacionPruebas } from './informacion-pruebas/informacion-pruebas.component';
import { MiCentro } from './mi-centro/mi-centro.component';
import { AdministrarCarpetasPruebasComunidad } from './administrar-carpetas-pruebas-comunidad/administrar-carpetas-pruebas-comunidad.component';
import { AnnadirPruebaComunidad } from './annadir-prueba-comunidad/annadir-prueba-comunidad.component';
import { AdministrarPruebasComunidad } from './administrar-pruebas-comunidad/administrar-pruebas-comunidad.component';
import { EstadisticasCentrosComunidad } from './estadisticas-centros-comunidad/estadisticas-centros-comunidad.component';
import { EstadisticasComunidad } from './estadisticas-comunidad/estadisticas-comunidad.component';

const routes: Routes = [
  { path: 'iniciarSesion', component: InicioSesion},
  { path: 'notificaciones', component: Notificaciones},
  { path: 'nueva-cita', component: NuevaCita},
  { path: 'citas-pendientes', component: CitasPendientes, canActivate:[ControladorPermisos]},
  { path: 'historico-citas', component: HistoricoCitas, canActivate:[ControladorPermisos]},
  { path: 'gestionar-cita', component: GestionarCita, canActivate:[ControladorPermisos]},
  { path: 'diario-citas', component: DiarioCitas, canActivate:[ControladorPermisos]},
  { path: 'estadisticas-enfermero', component: EstadisticasEnfermero, canActivate:[ControladorPermisos]},
  { path: 'estadisticas-empleados-centro', component: EstadisticasEmpleadosCentro, canActivate:[ControladorPermisos]},
  { path: 'estadisticas-centro', component: EstadisticasCentro, canActivate:[ControladorPermisos]},
  { path: 'estadisticas-centros-comunidad', component: EstadisticasCentrosComunidad, canActivate:[ControladorPermisos]},
  { path: 'estadisticas-comunidad', component: EstadisticasComunidad, canActivate:[ControladorPermisos]},
  { path: 'administrar-pruebas-centro', component: AdministrarPruebasCentro, canActivate:[ControladorPermisos]},
  { path: 'administrar-plantilla-centro', component: AdministrarPlantillaCentro, canActivate:[ControladorPermisos]},
  { path: 'administrar-pruebas-comunidad/pruebas', component: AdministrarPruebasComunidad, canActivate:[ControladorPermisos]},
  { path: 'administrar-pruebas-comunidad/carpetas-pruebas', component: AdministrarCarpetasPruebasComunidad, canActivate:[ControladorPermisos]},
  { path: 'administrar-pruebas-comunidad/nueva-prueba', component: AnnadirPruebaComunidad, canActivate:[ControladorPermisos]},
  { path: 'administrar-pruebas-comunidad/actualizar-prueba', component: AnnadirPruebaComunidad, canActivate:[ControladorPermisos]},
  { path: 'annadir-nuevo-empleado', component: AnnadirEmpleado, canActivate:[ControladorPermisos]},
  { path: 'actualizar-pruebas-empleado', component: AnnadirEmpleado, canActivate:[ControladorPermisos]},
  { path: 'annadir-nueva-prueba-centro', component: AnnadirPruebaCentro, canActivate:[ControladorPermisos]},
  { path: 'actualizar-horarios-prueba-centro', component: AnnadirPruebaCentro, canActivate:[ControladorPermisos]},
  { path: 'administrar-centros-comunidad', component: AdministrarCentrosComunidad, canActivate:[ControladorPermisos]},
  { path: 'annadir-centro-comunidad', component: AnnadirCentro, canActivate:[ControladorPermisos]},
  { path: 'actualizar-centro-comunidad', component: AnnadirCentro, canActivate:[ControladorPermisos]},
  { path: 'mi-centro', component: MiCentro, canActivate:[ControladorPermisos]},
  { path: 'informacion-pruebas', component: InformacionPruebas, canActivate:[ControladorPermisos]},
  { path: '', component: MenuPrincipal, canActivate:[ControladorPermisos]},
  { path: '**', pathMatch: 'full', redirectTo: 'iniciarSesion' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
