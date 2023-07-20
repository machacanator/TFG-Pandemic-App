import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrarPruebasComunidad } from './administrar-pruebas-comunidad.component';

describe('AdministrarPruebasComunidadComponent', () => {
  let component: AdministrarPruebasComunidad;
  let fixture: ComponentFixture<AdministrarPruebasComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdministrarPruebasComunidad ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdministrarPruebasComunidad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
