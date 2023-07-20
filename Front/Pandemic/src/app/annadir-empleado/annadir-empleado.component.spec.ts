import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnadirEmpleado } from './annadir-empleado.component';

describe('AnnadirEmpleadoComponent', () => {
  let component: AnnadirEmpleado;
  let fixture: ComponentFixture<AnnadirEmpleado>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnnadirEmpleado ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnadirEmpleado);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
