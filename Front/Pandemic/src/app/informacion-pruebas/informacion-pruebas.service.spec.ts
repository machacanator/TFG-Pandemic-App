import { TestBed } from '@angular/core/testing';

import { ServicioInformacionPruebas } from './informacion-pruebas.service';

describe('ServicioInformacionPruebas', () => {
  let service: ServicioInformacionPruebas;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioInformacionPruebas);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
