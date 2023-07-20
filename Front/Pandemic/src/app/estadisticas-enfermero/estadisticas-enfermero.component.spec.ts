import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstadisticasEnfermero } from './estadisticas-enfermero.component';

describe('EstadisticasEnfermero', () => {
  let component: EstadisticasEnfermero;
  let fixture: ComponentFixture<EstadisticasEnfermero>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstadisticasEnfermero ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstadisticasEnfermero);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
