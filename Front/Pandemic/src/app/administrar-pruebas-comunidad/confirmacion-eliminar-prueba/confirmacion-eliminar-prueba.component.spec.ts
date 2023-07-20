import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionEliminarPrueba } from './confirmacion-eliminar-prueba.component';

describe('ConfirmacionEliminarPrueba', () => {
  let component: ConfirmacionEliminarPrueba;
  let fixture: ComponentFixture<ConfirmacionEliminarPrueba>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmacionEliminarPrueba ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmacionEliminarPrueba);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
