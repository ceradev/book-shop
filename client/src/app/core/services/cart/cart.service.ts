import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environment/environment';
import { Cart } from '@models/Carts';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private baseUrl = `${environment.urlApi}/cart`;

  constructor(private http: HttpClient) { }


  getCart(): Observable<Cart> {
    return this.http.get<Cart>(`${this.baseUrl}`);
  }

  addItemToCart(isbn: string, quantity: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${isbn}/${quantity}`, null);
  }

  removeFromCart(isbn: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${isbn}`);
  }
  //El update sobra.
  updateCartBookQuantity(isbn: string, quantity: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${isbn}/${quantity}`, null);
  }

  clearCart(): Observable<any> {
    return this.http.delete(`${this.baseUrl}/clear`);
  }

}
