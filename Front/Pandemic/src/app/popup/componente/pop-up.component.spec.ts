import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopUp } from './pop-up.component';

describe('PopUpComponent', () => {
  let component: PopUp;
  let fixture: ComponentFixture<PopUp>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PopUp ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PopUp);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
