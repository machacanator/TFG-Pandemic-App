import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrarCentrosComunidad } from './administrar-centros-comunidad.component';

describe('AdministrarCentrosComunidadComponent', () => {
  let component: AdministrarCentrosComunidad;
  let fixture: ComponentFixture<AdministrarCentrosComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdministrarCentrosComunidad ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdministrarCentrosComunidad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
