import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionEliminarPruebaCentro } from './confirmacion-eliminar-prueba-centro.component';

describe('ConfirmacionEliminarPruebaCentroComponent', () => {
  let component: ConfirmacionEliminarPruebaCentro;
  let fixture: ComponentFixture<ConfirmacionEliminarPruebaCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmacionEliminarPruebaCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmacionEliminarPruebaCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
