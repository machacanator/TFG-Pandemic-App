import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InformacionPruebas } from './informacion-pruebas.component';

describe('InformacionPruebasComponent', () => {
  let component: InformacionPruebas;
  let fixture: ComponentFixture<InformacionPruebas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InformacionPruebas ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InformacionPruebas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
