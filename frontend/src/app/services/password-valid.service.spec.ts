import { TestBed } from '@angular/core/testing';

import { PasswordValidService } from './password-valid.service';

describe('PasswordValidService', () => {
  let service: PasswordValidService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PasswordValidService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
