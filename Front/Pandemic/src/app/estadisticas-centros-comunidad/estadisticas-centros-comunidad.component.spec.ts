import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstadisticasCentrosComunidad } from './estadisticas-centros-comunidad.component';

describe('EstadisticasCentrosComunidadComponent', () => {
  let component: EstadisticasCentrosComunidad;
  let fixture: ComponentFixture<EstadisticasCentrosComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstadisticasCentrosComunidad ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstadisticasCentrosComunidad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
