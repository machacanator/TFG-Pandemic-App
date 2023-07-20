import { TestBed } from '@angular/core/testing';

import { ServicioNuevaCita } from './servicio-nueva-cita.service';

describe('NuevaCitaService', () => {
  let service: ServicioNuevaCita;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioNuevaCita);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
