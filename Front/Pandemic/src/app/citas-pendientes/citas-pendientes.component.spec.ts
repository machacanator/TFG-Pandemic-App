import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CitasPendientes } from './citas-pendientes.component';

describe('CitasPendientesComponent', () => {
  let component: CitasPendientes;
  let fixture: ComponentFixture<CitasPendientes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CitasPendientes ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CitasPendientes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
