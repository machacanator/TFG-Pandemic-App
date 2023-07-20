import { TestBed } from '@angular/core/testing';

import { ServicioPopUp } from './servicio-pop-up.service';

describe('ServicioPopUpService', () => {
  let service: ServicioPopUp;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioPopUp);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
