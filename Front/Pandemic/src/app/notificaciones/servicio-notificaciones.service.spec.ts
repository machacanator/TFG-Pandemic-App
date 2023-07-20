import { TestBed } from '@angular/core/testing';

import { ServicioNotificaciones } from './servicio-notificaciones.service';

describe('ServicioNotificacionesService', () => {
  let service: ServicioNotificaciones;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioNotificaciones);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
