import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CabeceraCalendarioCitas } from './cabecera-calendario-citas.component';

describe('CabeceraCalendarioCitasComponent', () => {
  let component: CabeceraCalendarioCitas;
  let fixture: ComponentFixture<CabeceraCalendarioCitas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CabeceraCalendarioCitas ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CabeceraCalendarioCitas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
