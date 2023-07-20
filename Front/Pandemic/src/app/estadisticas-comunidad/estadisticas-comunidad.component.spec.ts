import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstadisticasComunidad } from './estadisticas-comunidad.component';

describe('EstadisticasComunidadComponent', () => {
  let component: EstadisticasComunidad;
  let fixture: ComponentFixture<EstadisticasComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstadisticasComunidad ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstadisticasComunidad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
