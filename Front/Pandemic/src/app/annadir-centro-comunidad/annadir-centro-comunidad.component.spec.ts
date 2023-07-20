import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnadirCentro } from './annadir-centro-comunidad.component';

describe('AnnadirCentroComponent', () => {
  let component: AnnadirCentro;
  let fixture: ComponentFixture<AnnadirCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnnadirCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnadirCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
