import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnadirPruebaComunidad } from './annadir-prueba-comunidad.component';

describe('AnnadirPruebaComunidadComponent', () => {
  let component: AnnadirPruebaComunidad;
  let fixture: ComponentFixture<AnnadirPruebaComunidad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnnadirPruebaComunidad ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnadirPruebaComunidad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
