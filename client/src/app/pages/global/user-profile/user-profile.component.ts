import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/services/auth/auth.service';
import { roleAuthenticationToken } from '../../../shared/hooks/roleAuthenticationToken';
import { AdminProfileComponent } from '../../admin/admin-profile/admin-profile.component';
import { SellerProfileComponent } from '../../seller/seller-profile/seller-profile.component';
import { ClientProfileComponent } from '../../client/client-profile/client-profile.component';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [ AdminProfileComponent, SellerProfileComponent, ClientProfileComponent],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent implements OnInit {
  isAuthenticated: boolean = false;
  role: any;

  constructor(private authService: AuthService) {}

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
}
