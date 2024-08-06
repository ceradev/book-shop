import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminViewClientComponent } from './admin-view-client.component';

describe('AdminViewClientComponent', () => {
  let component: AdminViewClientComponent;
  let fixture: ComponentFixture<AdminViewClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminViewClientComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminViewClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
