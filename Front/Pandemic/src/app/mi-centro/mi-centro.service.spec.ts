import { TestBed } from '@angular/core/testing';

import { ServicioMiCentro } from './mi-centro.service';

describe('MiCentroService', () => {
  let service: ServicioMiCentro;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioMiCentro);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
