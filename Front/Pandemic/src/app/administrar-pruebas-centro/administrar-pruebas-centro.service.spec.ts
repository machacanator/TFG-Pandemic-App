import { TestBed } from '@angular/core/testing';

import { ServicioAdministrarPruebasCentro } from './administrar-pruebas-centro.service';

describe('AdministrarPruebasCentroService', () => {
  let service: ServicioAdministrarPruebasCentro;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioAdministrarPruebasCentro);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
