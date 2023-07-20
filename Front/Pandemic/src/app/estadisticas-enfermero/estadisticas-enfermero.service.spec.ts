import { TestBed } from '@angular/core/testing';

import { ServicioEstadisticasEnfermero } from './estadisticas-enfermero.service';

describe('ServicioEstadisticasEnfermero', () => {
  let service: ServicioEstadisticasEnfermero;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioEstadisticasEnfermero);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
