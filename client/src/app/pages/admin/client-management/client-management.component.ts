import { Component, OnInit } from '@angular/core';
import { User } from '../../../core/models/User';
import { UserService } from '../../../core/services/user/user-service';
import { RouterModule } from '@angular/router';
import { SwlAlerts } from '../../../shared/utils/swl';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-client-management',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './client-management.component.html',
  styleUrl: './client-management.component.css',
})
export class ClientManagementComponent implements OnInit {
  users: User[] = [];
  isLoading: boolean = false;

  constructor(private userService: UserService, private swlAlerts: SwlAlerts) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.loadUsers();
    this.swlAlerts.showLoandingModal('Cargando lista de usuarios...');
  }

  loadUsers(filter: string = 'all') {
    this.userService.getAllUsers().subscribe((users: User[]) => {
      if (filter === 'admin') {
        this.users = users.filter((user: User) => user.role === 'ADMIN');
      } else if (filter === 'seller') {
        this.users = users.filter((user: User) => user.role === 'SELLER');
      } else if (filter === 'client') {
        this.users = users.filter((user: User) => user.role === 'CLIENT');
      } else {
        this.users = users;
      }
      for (const element of this.users) {
        if (element.role === 'CLIENT') {
          element.role = 'Cliente';
        } else if (element.role === 'ADMIN') {
          element.role = 'Administrador';
        } else if (element.role === 'SELLER') {
          element.role = 'Vendedor';
        }
      }
      this.isLoading = false;
      Swal.close();
    });
  }

  changeFilter($event: any) {
    this.isLoading = true;
    this.swlAlerts.showLoandingModal('Cargando lista de usuarios...');
    this.loadUsers($event.target.value);
  }
}
