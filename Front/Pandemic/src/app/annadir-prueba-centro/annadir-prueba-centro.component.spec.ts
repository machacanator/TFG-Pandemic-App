import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnadirPruebaCentro } from './annadir-prueba-centro.component';

describe('AnnadirPruebaCentroComponent', () => {
  let component: AnnadirPruebaCentro;
  let fixture: ComponentFixture<AnnadirPruebaCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnnadirPruebaCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnadirPruebaCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
