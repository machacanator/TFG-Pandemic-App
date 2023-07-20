import { TestBed } from '@angular/core/testing';

import { ServicioAdministrarCentrosComunidad } from './administrar-centros-comunidad.service';

describe('AdministrarCentrosComunidadService', () => {
  let service: ServicioAdministrarCentrosComunidad;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioAdministrarCentrosComunidad);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
