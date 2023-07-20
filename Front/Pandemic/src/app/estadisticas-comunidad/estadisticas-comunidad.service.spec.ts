import { TestBed } from '@angular/core/testing';

import { ServiciosEstadisticasComunidad } from './estadisticas-comunidad.service';

describe('ServiciosEstadisticasComunidad', () => {
  let service: ServiciosEstadisticasComunidad;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiciosEstadisticasComunidad);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
