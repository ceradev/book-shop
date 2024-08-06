import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminHomeComponent } from '@pages/admin/admin-home/admin-home.component';
import { SellerHomeComponent } from '@pages/seller/seller-home/seller-home.component';
import { ClientHomeComponent } from '@pages/client/client-home/client-home.component';
import { AuthService } from '@services/auth/auth.service';
import { roleAuthenticationToken } from '@hooks/roleAuthenticationToken';



@Component({
    selector: 'app-home',
    standalone: true,
    templateUrl: './home.component.html',
    styleUrl: './home.component.css',
    imports: [CommonModule, AdminHomeComponent, SellerHomeComponent, ClientHomeComponent],
})
export class HomeComponent implements OnInit{

  isAuthenticated: boolean = false;
  role: any;

  constructor(private authService: AuthService) { }

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
