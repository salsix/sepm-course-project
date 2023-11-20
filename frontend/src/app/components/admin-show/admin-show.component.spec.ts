import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminShowComponent } from './admin-show.component';

describe('ShowComponent', () => {
  let component: AdminShowComponent;
  let fixture: ComponentFixture<AdminShowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminShowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
