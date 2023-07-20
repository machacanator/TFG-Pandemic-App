import { TestBed } from '@angular/core/testing';

import { ServicioEstadisticasCentro } from './estadisticas-centro.service';

describe('EstadisticasEmpleadosCentroService', () => {
  let service: ServicioEstadisticasCentro;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioEstadisticasCentro);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
