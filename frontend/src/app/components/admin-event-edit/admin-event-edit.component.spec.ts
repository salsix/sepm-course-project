import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEventEditComponent } from './admin-event-edit.component';

describe('EventEditComponent', () => {
  let component: AdminEventEditComponent;
  let fixture: ComponentFixture<AdminEventEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminEventEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminEventEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
