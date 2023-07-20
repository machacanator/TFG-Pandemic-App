import { TestBed } from '@angular/core/testing';

import { ControladorPermisos } from './controlador-permisos.guard';

describe('ControladorPermisosGuard', () => {
  let guard: ControladorPermisos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(ControladorPermisos);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
