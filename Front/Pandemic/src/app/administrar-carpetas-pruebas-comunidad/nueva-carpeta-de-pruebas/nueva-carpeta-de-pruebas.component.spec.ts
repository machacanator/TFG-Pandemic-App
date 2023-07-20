import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaCarpetaDePruebas } from './nueva-carpeta-de-pruebas.component';

describe('NuevaCarpetaDePruebas', () => {
  let component: NuevaCarpetaDePruebas;
  let fixture: ComponentFixture<NuevaCarpetaDePruebas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NuevaCarpetaDePruebas ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NuevaCarpetaDePruebas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
