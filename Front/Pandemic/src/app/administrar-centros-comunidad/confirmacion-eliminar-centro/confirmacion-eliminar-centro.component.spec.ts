import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionEliminarCentro } from './confirmacion-eliminar-centro.component';

describe('ConfirmacionEliminarEmpleado', () => {
  let component: ConfirmacionEliminarCentro;
  let fixture: ComponentFixture<ConfirmacionEliminarCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmacionEliminarCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmacionEliminarCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
