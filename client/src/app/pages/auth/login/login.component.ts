import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth/auth.service';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SwlAlerts } from '../../../shared/utils/swl';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  imports: [HttpClientModule, FormsModule, RouterModule],
  providers: [HttpClient, AuthService],
})
export class LoginComponent {
  username: string;
  password: string;
  isLoading: boolean  = false;

  constructor(private authService: AuthService, private router: Router, private swlAlerts: SwlAlerts) {}

  ngOnInit() {
    this.username = '';
    this.password = '';
  }

  onSubmit() {
    if(this.username === '' && this.password === '') {
      this.swlAlerts.showToastError('Faltan campos por rellenar o los campos introducidos no son válidos. Inténtalo de nuevo');
      return;
    }
    else if (this.username === '') {
      this.swlAlerts.showToastError('Por favor, introduce tu nombre de usuario');
      return;
    } else if (this.password === '') {
      this.swlAlerts.showToastError('Por favor, introduce tu contraseña');
      return;
    } 
    this.isLoading = true;
    this.authService.login(this.username, this.password).subscribe({
      next: (response: any) => {
        // Handle login success here
        sessionStorage.setItem('ACCESS_TOKEN', response.body.access_token);
        sessionStorage.setItem('REFRESH_TOKEN', response.body.refresh_token);
        this.swlAlerts.showToastSuccess('Se ha iniciado sesión correctamente');
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: (error) => {
        // Handle login failure here
        this.swlAlerts.showToastError('Credenciales incorrectas. Inténtalo de nuevo');
        this.isLoading = false;
      },
    });;
  }
}
