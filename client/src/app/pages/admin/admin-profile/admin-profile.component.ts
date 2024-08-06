import { Component, OnInit } from '@angular/core';
import { StarsComponent } from '../../../shared/components/stars/stars.component';
import { AuthService } from '../../../core/services/auth/auth.service';
import { Router } from '@angular/router';
import { User } from '../../../core/models/User';
import { UserService } from '../../../core/services/user/user-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UpdateUserRequest } from '../../../core/models/update-user-request';
import { jwtDecode } from 'jwt-decode';
import { SwlAlerts } from '../../../shared/utils/swl';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-profile',
  standalone: true,
  imports: [StarsComponent, CommonModule, FormsModule],
  templateUrl: './admin-profile.component.html',
  styleUrl: './admin-profile.component.css',
})
export class AdminProfileComponent implements OnInit {
  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService,
    private swlAlerts: SwlAlerts
  ) {}

  user: User = {
    id: '',
    username: '',
    name: '',
    surname: '',
    email: '',
    password: '',
    avatar: '',
    role: '',
    addresses: [],
  };

  username: string;
  name: string;
  surname: string;
  email: string;
  oldPassword: string;
  newPassword: string;
  repeatNewPassword: string;

  usernameForm: boolean = false;
  nameForm: boolean = false;
  surnameForm: boolean = false;
  emailForm: boolean = false;
  passwordForm: boolean = false;

  ngOnInit(): void {
    AuthService.isAdmin().subscribe((isAdmin: boolean) => {
      if (!isAdmin) {
        this.router.navigate(['/']);
      } else {
        this.userService.getUser().subscribe((user: User) => {
          this.user = user;
          this.username = user.username;
          this.name = user.name;
          this.surname = user.surname;
          this.email = user.email;
          console.log(this.user);
        });
      }
    });
  }

  updateProfile() {
    const updatedUser: UpdateUserRequest = {
      username: this.username,
      name: this.name,
      surname: this.surname,
    };

    this.userService.modifyUser(updatedUser).subscribe({
      next: () => {
        this.swlAlerts.showToastSuccess('Tu perfil ha sido actualizado correctamente');
        this.user.username = this.username;
        this.user.name = this.name;
        this.user.surname = this.surname;
        this.user.email = this.email;
      },
      error: () => {
        this.swlAlerts.showToastError('No se ha podido actualizar el perfil');
      }
    });
  }

  resetPassword() {
    if (this.newPassword !== this.repeatNewPassword) {
      this.swlAlerts.showToastError('Las contraseñas no coinciden');
      return;
    }
    this.authService.resetPassword(
      this.user.username,
      this.oldPassword,
      this.newPassword
    ).subscribe({
      next: () => {
        this.swlAlerts.showLoandingModal('Cambiando contraseña, se redirigira en 3 segundos...');
        setTimeout(() => {
          this.authService.logout();
          Swal.close();
        }, 3000);
      },
      error: () => {
        this.swlAlerts.showToastError('No se ha podido cambiar la contraseña. Inténtalo de nuevo');
      }
    });
  }

  unChangeUsername() {
    this.username = this.user.username;
    this.usernameForm = false;
  }
  unChangeName() {
    this.name = this.user.name;
    this.nameForm = false;
  }
  unChangeSurname() {
    this.surname = this.user.surname;
    this.surnameForm = false;
  }
}
