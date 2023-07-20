import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiCentro } from './mi-centro.component';

describe('MiCentroComponent', () => {
  let component: MiCentro;
  let fixture: ComponentFixture<MiCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MiCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MiCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
