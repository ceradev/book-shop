import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateSimpleComponent } from './template-simple.component';

describe('TemplateSimpleComponent', () => {
  let component: TemplateSimpleComponent;
  let fixture: ComponentFixture<TemplateSimpleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateSimpleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TemplateSimpleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
