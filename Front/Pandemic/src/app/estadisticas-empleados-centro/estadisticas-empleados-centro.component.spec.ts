import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstadisticasEmpleadosCentro } from './estadisticas-empleados-centro.component';

describe('EstadisticasEmpleadosComponent', () => {
  let component: EstadisticasEmpleadosCentro;
  let fixture: ComponentFixture<EstadisticasEmpleadosCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstadisticasEmpleadosCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstadisticasEmpleadosCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
