import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymeComponent } from './PaymeComponent';

describe('PaymeComponent', () => {
  let component: PaymeComponent;
  let fixture: ComponentFixture<PaymeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaymeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaymeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
