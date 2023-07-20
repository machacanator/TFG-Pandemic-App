import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuPrincipal } from './menu-principal.component';

describe('MenuPrincipalComponent', () => {
  let component: MenuPrincipal;
  let fixture: ComponentFixture<MenuPrincipal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MenuPrincipal ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MenuPrincipal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
