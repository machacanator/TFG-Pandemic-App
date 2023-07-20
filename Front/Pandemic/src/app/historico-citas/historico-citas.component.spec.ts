import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricoCitas } from './historico-citas.component';

describe('HistoricoCitasComponent', () => {
  let component: HistoricoCitas;
  let fixture: ComponentFixture<HistoricoCitas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoricoCitas ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoricoCitas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
