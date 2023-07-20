import { TestBed } from '@angular/core/testing';

import { ServicioTokensService } from './servicio-tokens.service';

describe('ServicioTokensService', () => {
  let service: ServicioTokensService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioTokensService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
