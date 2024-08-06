import { Component } from '@angular/core';
import { NavbarComponent } from "../../navbar/navbar.component";
import { FooterComponent } from "../../footer/footer.component";
import { RouterOutlet } from '@angular/router';

@Component({
    selector: 'app-template-simple',
    standalone: true,
    templateUrl: './template-simple.component.html',
    styleUrl: './template-simple.component.css',
    imports: [NavbarComponent, FooterComponent, RouterOutlet]
})
export class TemplateSimpleComponent {

}
