import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth/auth.service';
import { CreateUserRequest } from '../../../core/models/create-user-request';
import { Router, RouterLink } from '@angular/router';
import { SwlAlerts } from '../../../shared/utils/swl';


@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  imports: [CommonModule, FormsModule, RouterLink],
})
export class RegisterComponent {
  name: string = '';
  surname: string = '';
  username: string = '';
  email: string = '';
  password: string = '';
  repeatPassword: string = '';
  selectedRoles: string[] = [];

onRoleChange(event: any) {
  this.selectedRoles = [];  // Clear previous selection
  this.selectedRoles.push(event.target.value);  // Add selected role
}
  constructor(private authService: AuthService, private router: Router, private swlAlerts: SwlAlerts) {}

  onSubmit() {
    if (
      this.name === '' ||
      this.surname === '' ||
      this.username === '' ||
      this.email === '' ||
      this.password === '' ||
      this.repeatPassword === ''
    ) {
      this.swlAlerts.showToastError('Faltan campos por rellenar o los campos introducidos no son válidos. Inténtalo de nuevo');
      return;
    }

    if (this.email.indexOf('@') === -1 || this.email.indexOf('.') === -1) {
      this.swlAlerts.showToastError('El correo electrónico no es válido');
      return;
    }

    if (this.password.length < 8) {
      this.swlAlerts.showToastError('La contraseña debe tener al menos 8 caracteres');
      return;
    }

    if (this.selectedRoles.length === 0) {
      this.swlAlerts.showToastError('Debes seleccionar al menos un rol');
      return;
    }

    if (this.password !== this.repeatPassword) {
      this.swlAlerts.showToastError('Las contraseñas no coinciden');
      return;
    }

    const user: CreateUserRequest = {
      name: this.name,
      surname: this.surname,
      username: this.username,
      email: this.email,
      password: this.password,
      roles: this.selectedRoles,
    };

    this.authService.register(user).subscribe({
      next: (response: any) => {
        // Handle registration success here
        sessionStorage.setItem('ACCESS_TOKEN', response.access_token);
        sessionStorage.setItem('REFRESH_TOKEN', response.refresh_token);
        this.router.navigate(['/']);
      },
      error: (error) => {
        // Handle registration failure here
        if (error.status === 400) {
          this.swlAlerts.showToastError('Faltan campos por rellenar o los campos introducidos no son válidos. Inténtalo de nuevo');
          console.error(error);
        } else if (error.status === 409) {
          this.swlAlerts.showToastError('El nombre de usuario o el correo electrónico ya existe');
          console.error(error);
        } else {
          this.swlAlerts.showToastError('Error al registrar el usuario. Inténtalo de nuevo');
          console.error(error);
        }
      },
    });;
  }
}
