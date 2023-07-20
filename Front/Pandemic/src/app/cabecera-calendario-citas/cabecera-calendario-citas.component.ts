import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { DateAdapter, MatDateFormats, MAT_DATE_FORMATS } from '@angular/material/core';
import { MatCalendar } from '@angular/material/datepicker';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Subject, takeUntil } from 'rxjs';
import { ServicioNuevaCita } from '../nueva-cita/servicio-nueva-cita.service';

@Component({
  selector: 'app-cabecera-calendario-citas',
  templateUrl: './cabecera-calendario-citas.component.html',
  styleUrls: ['./cabecera-calendario-citas.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CabeceraCalendarioCitas implements OnDestroy {

  anteriorMesDisponible: boolean = false;
  anteriorAnnoDisponible: boolean = false;

  private eliminarComponente = new Subject<void>();

  constructor(
    private calendario: MatCalendar<Moment>,
    private adaptadorFecha: DateAdapter<Moment>,
    private servicioCitas: ServicioNuevaCita,
    @Inject(MAT_DATE_FORMATS) private formatoFechas: MatDateFormats,
    detectorDeCambios: ChangeDetectorRef,
  ) {
    calendario.stateChanges.pipe(takeUntil(this.eliminarComponente)).subscribe(() => detectorDeCambios.markForCheck());
  }

  private actualizarVariables() {
    this.anteriorAnnoDisponible = this.calendario.activeDate.year() >= moment().year()+2
      || (this.calendario.activeDate.year() === (moment().year()+1) && this.calendario.activeDate.month() >= moment().month());
    this.anteriorMesDisponible = this.calendario.activeDate.month() > moment().month()
      || this.calendario.activeDate.year() > moment().year();
  }

  ngOnDestroy() {
    this.eliminarComponente.next();
    this.eliminarComponente.complete();
  }

  get periodLabel() {
    return this.adaptadorFecha
      .format(this.calendario.activeDate, this.formatoFechas.display.monthYearLabel)
      .toLocaleUpperCase();
  }

  clickAnterior(modo: 'mes' | 'anno') {
    this.calendario.activeDate =
      modo === 'mes'
        ? this.adaptadorFecha.addCalendarMonths(this.calendario.activeDate, -1)
        : this.adaptadorFecha.addCalendarYears(this.calendario.activeDate, -1);
    this.actualizarVariables();
    this.servicioCitas.cambioCabeceraCalendario.next(this.calendario.activeDate);
  }

  clickSiguiente(modo: 'mes' | 'anno') {
    this.calendario.activeDate =
      modo === 'mes'
        ? this.adaptadorFecha.addCalendarMonths(this.calendario.activeDate, 1)
        : this.adaptadorFecha.addCalendarYears(this.calendario.activeDate, 1);
    this.actualizarVariables();
    this.servicioCitas.cambioCabeceraCalendario.next(this.calendario.activeDate);
  }

}
