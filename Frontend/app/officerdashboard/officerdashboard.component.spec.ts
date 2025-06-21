import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficerdashboardComponent } from './officerdashboard.component';

describe('OfficerdashboardComponent', () => {
  let component: OfficerdashboardComponent;
  let fixture: ComponentFixture<OfficerdashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfficerdashboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OfficerdashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
