import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiarioCitas } from './diario-citas.component';

describe('DiarioCitasComponent', () => {
  let component: DiarioCitas;
  let fixture: ComponentFixture<DiarioCitas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DiarioCitas ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiarioCitas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
