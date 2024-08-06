import { TestBed } from '@angular/core/testing';

import { SendFiltersService } from './send-filters.service';

describe('SendFiltersService', () => {
  let service: SendFiltersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SendFiltersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
