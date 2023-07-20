import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrarPlantillaCentro } from './administrar-plantilla-centro.component';

describe('AdministrarPlantillaCentroComponent', () => {
  let component: AdministrarPlantillaCentro;
  let fixture: ComponentFixture<AdministrarPlantillaCentro>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdministrarPlantillaCentro ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdministrarPlantillaCentro);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
