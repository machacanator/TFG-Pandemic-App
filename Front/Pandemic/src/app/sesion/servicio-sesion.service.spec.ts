import { TestBed } from '@angular/core/testing';

import { ServicioSesion } from './servicio-sesion';

describe('ServicioSesionService', () => {
  let service: ServicioSesion;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioSesion);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
