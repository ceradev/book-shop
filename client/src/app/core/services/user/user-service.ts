import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { User } from '../../models/User';
import { UpdateUserRequest } from '../../models/update-user-request';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http:HttpClient) { }
  

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(environment.urlApi + '/users');
  }

  getUser():Observable<User>{
    return this.http.get(environment.urlApi + "/users/me").pipe(
      map(response => response as User)
    );
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(`${environment.urlApi + '/users'}/${id}`);
  }

  modifyUser(user:UpdateUserRequest):Observable<User>{
    return this.http.put<User>(environment.urlApi + '/users/me', user);
  }
}
