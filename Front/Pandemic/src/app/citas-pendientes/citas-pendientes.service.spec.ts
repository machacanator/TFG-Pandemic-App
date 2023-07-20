import { TestBed } from '@angular/core/testing';

import { ServicioCitasPendientes } from './citas-pendientes.service';

describe('CitaspendientesService', () => {
  let service: ServicioCitasPendientes;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioCitasPendientes);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
