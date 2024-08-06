import { Cart } from '@models/Carts';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environment/environment';
import { Observable, catchError, map, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PaypalService {
  private baseUrl = `${environment.urlApi}/payment`;

  constructor(private http: HttpClient) {}

  createOrder(cartId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/create-order?cartId=${cartId}`, null).pipe(
      map((response) => {
        console.log('Response from server:', response);
        return response;
      }),
      catchError((error) => {
        console.error('Error in createOrder:', error);
        return throwError(error);
      })
    );
  }

  completeOrder(token: string): Observable<any> {
    return this.http.get(
      `${this.baseUrl}/success?token=${token}`
    );
  }
}
