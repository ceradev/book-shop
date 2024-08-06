import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CreateUserRequest } from '../../models/create-user-request';
import { BehaviorSubject, Observable, catchError, map, of } from 'rxjs';
import { roleAuthenticationToken } from '../../../shared/hooks/roleAuthenticationToken';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    return this.http.post(
      environment.urlAuth + '/login',
      { username, password },
      { observe: 'response' }
    );
  }

  register(user: CreateUserRequest) {
    return this.http.post(environment.urlAuth + '/register', user);
  }

  logout() {
    this.http.post(environment.urlAuth + '/logout', {}).subscribe(
      () => {
        sessionStorage.removeItem('ACCESS_TOKEN');
        sessionStorage.removeItem('REFRESH_TOKEN');
        this.isAuthenticatedSubject.next(false);
        if (window.location.pathname == '/') {
          window.location.reload();
        } else if (window.location.pathname == '/profile') {
          this.router.navigate(['/login']);
        } else {
          console.log("No se ha podido cerrar la sesión");
          this.router.navigate(['/']);
        }
      },
      (error) => {
        console.error('Error:', error);
      }
    );
  }
  resetPassword(username: string, oldPassword: string, newPassword: string) {
    return this.http.post(environment.urlAuth + '/reset-password', {
      username,
      oldPassword,
      newPassword,
    });
  }
  isAuthenticated(): Observable<boolean> {
    if (sessionStorage.getItem('ACCESS_TOKEN')) {
      return this.http.get(environment.urlAuth + '/me').pipe(
        map((response: any) => {
          // Handle authentication success here
          this.isAuthenticatedSubject.next(true);
          return true;
        }),
        catchError((error) => {
          this.isAuthenticatedSubject.next(false);
          return of(false);
        })
      );
    } else {
      this.isAuthenticatedSubject.next(false);
      return of(false);
    }
  }

  static isClient(): Observable<boolean> {
    if (
      roleAuthenticationToken(sessionStorage.getItem('ACCESS_TOKEN')!) ===
      'client'
    ) {
      return of(true);
    } else {
      return of(false);
    }
  }

  static isSeller(): Observable<boolean> {
    if (
      roleAuthenticationToken(sessionStorage.getItem('ACCESS_TOKEN')!) ===
      'seller'
    ) {
      return of(true);
    } else {
      return of(false);
    }
  }

  static isAdmin(): Observable<boolean> {
    if (
      roleAuthenticationToken(sessionStorage.getItem('ACCESS_TOKEN')!) ===
      'admin'
    ) {
      return of(true);
    } else {
      return of(false);
    }
  }

  static decodeToken(): any {
    return jwtDecode(sessionStorage.getItem('ACCESS_TOKEN')!);
  }
}
