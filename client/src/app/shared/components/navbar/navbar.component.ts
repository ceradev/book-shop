import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { roleAuthenticationToken } from '../../hooks/roleAuthenticationToken';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth/auth.service';
import { SwlAlerts } from '../../utils/swl';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent implements OnInit {
  isAuthenticated: boolean = false;
  role: any;
  dropdownOpen: boolean = false;

  constructor(private authService: AuthService, private swlAlerts: SwlAlerts) {}

  ngOnInit(): void {
    if (sessionStorage.getItem('ACCESS_TOKEN') != null) {
      this.authService.isAuthenticated().subscribe((isAuthenticated) => {
        if (isAuthenticated === false) {
          this.isAuthenticated = false;
          sessionStorage.removeItem('ACCESS_TOKEN');
          sessionStorage.removeItem('REFRESH_TOKEN');
          return;
        }
        this.role = roleAuthenticationToken(
          sessionStorage.getItem('ACCESS_TOKEN')!
        );
        this.isAuthenticated = true;
      });
    } else {
      this.isAuthenticated = false;
    }
  }

  logout() {
    this.swlAlerts.showLoandingModal('Cerrando sesión...');
    setTimeout(() => {
      this.authService.logout();
      Swal.close();
    }, 1000);
    this.isAuthenticated = false;
    this.dropdownOpen = false;
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }
}
