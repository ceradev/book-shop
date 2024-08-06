// cart-shared.service.ts

import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Cart } from '@models/Carts';

@Injectable({
  providedIn: 'root'
})
export class CartSharedService {
  private cartSubject = new BehaviorSubject<Cart>(null!);
  cart$ = this.cartSubject.asObservable();

  constructor() {}

  updateCart(cart: Cart) {
    this.cartSubject.next(cart);
  }
}
