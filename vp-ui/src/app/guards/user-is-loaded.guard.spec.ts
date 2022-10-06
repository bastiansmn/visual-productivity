import { TestBed } from '@angular/core/testing';

import { CanConfirmAccount } from './can-confirm-account.service';

describe('UserIsLoadedGuard', () => {
  let guard: CanConfirmAccount;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(CanConfirmAccount);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
