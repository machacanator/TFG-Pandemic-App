import { TestBed } from '@angular/core/testing';

import { ServicioDocumentos } from './servicio-documentos.service';

describe('ServicioDocumentosService', () => {
  let service: ServicioDocumentos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioDocumentos);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
