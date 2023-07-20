import { TestBed } from '@angular/core/testing';

import { ServicioDiarioCitas } from './diario-citas.service';

describe('DiariocitasService', () => {
  let service: ServicioDiarioCitas;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioDiarioCitas);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
