import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaCita } from './nueva-cita.component';

describe('NuevaCitaComponent', () => {
  let component: NuevaCita;
  let fixture: ComponentFixture<NuevaCita>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NuevaCita ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NuevaCita);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
