import { TestBed } from '@angular/core/testing';

import { ServicioAdministrarPruebasComunidad } from './administrar-pruebas-comunidad.service';

describe('AdministrarPruebasComunidadService', () => {
  let service: ServicioAdministrarPruebasComunidad;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioAdministrarPruebasComunidad);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
