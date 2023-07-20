import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionEliminarEmpleado } from './confirmacion-eliminar-empleado.component';

describe('ConfirmacionEliminarEmpleado', () => {
  let component: ConfirmacionEliminarEmpleado;
  let fixture: ComponentFixture<ConfirmacionEliminarEmpleado>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmacionEliminarEmpleado ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmacionEliminarEmpleado);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
