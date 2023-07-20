import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarCita } from './gestionar-cita.component';

describe('GestionarCitaComponent', () => {
  let component: GestionarCita;
  let fixture: ComponentFixture<GestionarCita>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GestionarCita ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionarCita);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
