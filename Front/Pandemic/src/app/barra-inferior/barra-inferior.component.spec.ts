import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarraInferior } from './barra-inferior.component';

describe('BarraInferiorComponent', () => {
  let component: BarraInferior;
  let fixture: ComponentFixture<BarraInferior>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BarraInferior ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarraInferior);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
