import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmacionEliminarCarpetaDePruebas } from './confirmacion-eliminar-carpeta-de-pruebas.component';

describe('ConfirmacionEliminarCarpetaDePruebas', () => {
  let component: ConfirmacionEliminarCarpetaDePruebas;
  let fixture: ComponentFixture<ConfirmacionEliminarCarpetaDePruebas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmacionEliminarCarpetaDePruebas ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmacionEliminarCarpetaDePruebas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
