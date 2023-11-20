import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminShowEditComponent } from './admin-show-edit.component';

describe('ShowEditComponent', () => {
  let component: AdminShowEditComponent;
  let fixture: ComponentFixture<AdminShowEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminShowEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminShowEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
