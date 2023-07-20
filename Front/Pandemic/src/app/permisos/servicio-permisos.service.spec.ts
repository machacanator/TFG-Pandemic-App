import { TestBed } from '@angular/core/testing';

import { ServicioSecciones } from './servicio-permisos.service';

describe('ServicioPermisosService', () => {
  let service: ServicioSecciones;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioSecciones);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
