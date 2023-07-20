import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrarCarpetasPruebasComunidad } from './administrar-carpetas-pruebas-comunidad.component';

describe('AdministrarCarpetasPruebasComunidadComponent', () => {
  let component: AdministrarCarpetasPruebasComunidad;
  let fixture: ComponentFixture<AdministrarCarpetasPruebasComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdministrarCarpetasPruebasComunidad ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdministrarCarpetasPruebasComunidad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
