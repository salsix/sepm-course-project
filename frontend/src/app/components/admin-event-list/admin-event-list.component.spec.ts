import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEventListComponent } from './admin-event-list.component';

describe('EventListComponent', () => {
  let component: AdminEventListComponent;
  let fixture: ComponentFixture<AdminEventListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminEventListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminEventListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
