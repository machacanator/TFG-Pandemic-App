import { TestBed } from '@angular/core/testing';

import { ServicioGestionarCita } from './gestionar-cita.service';

describe('ServicioGestionarCita', () => {
  let service: ServicioGestionarCita;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioGestionarCita);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
