import { TestBed } from '@angular/core/testing';

import { ServicioHistoricoCitas } from './historico-citas.service';

describe('HistoricoCitasService', () => {
  let service: ServicioHistoricoCitas;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioHistoricoCitas);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
