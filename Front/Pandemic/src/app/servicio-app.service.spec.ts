import { TestBed } from '@angular/core/testing';

import { ServicioAplicacion } from './servicio-app.service';

describe('ServicioAppService', () => {
  let service: ServicioAplicacion;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioAplicacion);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
