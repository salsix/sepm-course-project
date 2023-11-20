import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUsermanagementComponent } from './admin-usermanagement.component';

describe('AdminUsermanagementComponent', () => {
  let component: AdminUsermanagementComponent;
  let fixture: ComponentFixture<AdminUsermanagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminUsermanagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUsermanagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
