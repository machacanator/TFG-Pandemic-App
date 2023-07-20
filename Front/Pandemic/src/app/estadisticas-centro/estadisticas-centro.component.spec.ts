import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstadisticasCentro } from './estadisticas-centro.component';

describe('EstadisticasCentroComponent', () => {
  let component: EstadisticasCentro;
  let fixture: ComponentFixture<EstadisticasCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EstadisticasCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstadisticasCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
