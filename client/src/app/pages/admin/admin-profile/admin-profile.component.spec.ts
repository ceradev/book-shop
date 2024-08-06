import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProfileComponent } from './admin-profile.component';
import { By } from '@angular/platform-browser';

describe('AdminProfileComponent', () => {
  let component: AdminProfileComponent;
  let fixture: ComponentFixture<AdminProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have the inital text', () => {
    const cardElement: HTMLElement = fixture.nativeElement.query(By.css('.etiquetaPrueba'));

    expect(cardElement.textContent).toContain('pendientes')
  });
});
