import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrarPruebasCentro } from './administrar-pruebas-centro.component';

describe('AdministrarPruebasCentroComponent', () => {
  let component: AdministrarPruebasCentro;
  let fixture: ComponentFixture<AdministrarPruebasCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdministrarPruebasCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdministrarPruebasCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
