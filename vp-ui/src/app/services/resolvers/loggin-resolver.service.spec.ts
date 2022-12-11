import { TestBed } from '@angular/core/testing';

import { LogginResolverService } from './login-resolver.service';

describe('LogginResolverService', () => {
  let service: LogginResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LogginResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
