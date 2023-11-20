import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewsPreviousComponent } from './news-previous.component';

describe('NewsPreviousComponent', () => {
  let component: NewsPreviousComponent;
  let fixture: ComponentFixture<NewsPreviousComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewsPreviousComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewsPreviousComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
