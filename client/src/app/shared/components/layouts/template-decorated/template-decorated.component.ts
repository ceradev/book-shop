import { Component } from '@angular/core';
import { FooterComponent } from '../../footer/footer.component';
import { RouterOutlet } from '@angular/router';
import { AuthNavbarComponent } from '../../auth-navbar/auth-navbar.component';

@Component({
  selector: 'app-template-decorated',
  standalone: true,
  imports: [AuthNavbarComponent,FooterComponent,RouterOutlet],
  templateUrl: './template-decorated.component.html',
  styleUrl: './template-decorated.component.css'
})
export class TemplateDecoratedComponent {

}
