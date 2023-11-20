import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewsCurrentComponent } from './news-current.component';

describe('NewsCurrentComponent', () => {
  let component: NewsCurrentComponent;
  let fixture: ComponentFixture<NewsCurrentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewsCurrentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewsCurrentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
