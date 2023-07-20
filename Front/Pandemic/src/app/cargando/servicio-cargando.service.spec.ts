import { TestBed } from '@angular/core/testing';

import { ServicioCargandoService } from './servicio-cargando.service';

describe('ServicioCargandoService', () => {
  let service: ServicioCargandoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioCargandoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
