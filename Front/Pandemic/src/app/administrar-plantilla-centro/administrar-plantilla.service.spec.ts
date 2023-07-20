import { TestBed } from '@angular/core/testing';

import { ServicioAdministrarPlantilla } from './administrar-plantilla.service';

describe('ServicioAdministrarPlantilla', () => {
  let service: ServicioAdministrarPlantilla;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioAdministrarPlantilla);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
