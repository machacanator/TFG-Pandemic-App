import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InicioSesion } from './inicio-sesion.component';

describe('InicioSesionComponent', () => {
  let component: InicioSesion;
  let fixture: ComponentFixture<InicioSesion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InicioSesion ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InicioSesion);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
