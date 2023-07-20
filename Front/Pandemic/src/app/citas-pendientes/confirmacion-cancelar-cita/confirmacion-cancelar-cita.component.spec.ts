import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionCancelarCita } from './confirmacion-cancelar-cita.component';

describe('ConfirmacionCancelarCitaComponent', () => {
  let component: ConfirmacionCancelarCita;
  let fixture: ComponentFixture<ConfirmacionCancelarCita>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmacionCancelarCita ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmacionCancelarCita);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
