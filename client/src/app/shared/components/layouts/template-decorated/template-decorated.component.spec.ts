import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateDecoratedComponent } from './template-decorated.component';

describe('TemplateDecoratedComponent', () => {
  let component: TemplateDecoratedComponent;
  let fixture: ComponentFixture<TemplateDecoratedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateDecoratedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TemplateDecoratedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
