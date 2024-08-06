import { Component } from '@angular/core';
import { StarsComponent } from '../../../shared/components/stars/stars.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../core/services/user/user-service';
import { User } from '../../../core/models/User';
import { ActivatedRoute, Router } from '@angular/router';
import { AddressService } from '@services/address/address.service';

@Component({
  selector: 'app-admin-view-client',
  standalone: true,
  imports: [StarsComponent, CommonModule, FormsModule],
  templateUrl: './admin-view-client.component.html',
  styleUrl: './admin-view-client.component.css',
})
export class AdminViewClientComponent {
  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private addressService: AddressService
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

  ngOnInit(): void {
    this.userService
      .getUserById(this.activatedRoute.snapshot.params['id'])
      .subscribe((user: User) => {
        this.user = user;
        if(this.user.role == "seller"){
          this.user.role = "Vendedor";
        } else if(this.user.role == "admin"){
          this.user.role = "Administrador";
        } else {
          this.user.role = "Cliente";
        }
        this.addressService
          .getAllAddressesByUserId(this.user.id)
          .subscribe((addresses) => {
            this.user.addresses = addresses;
          });
      });
  }

  goBack() {
    this.router.navigate(['/clients']);
  }
}
