import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { Router, RouterModule } from '@angular/router';
import { Cart } from '@models/Carts';
import { CartService } from '@services/cart/cart.service';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { AuthService } from '@services/auth/auth.service';
import { SwlAlerts } from '@utils/swl';
import { CartBook } from '@models/Cart-book';
import { CartSharedService } from '@shared/services/cart/cart-shared.service';
import { PaymeComponent } from '@components/payme/payme.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [RouterModule, FormsModule, MatFormFieldModule, MatSelectModule, PaymeComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
})
export class CartComponent implements OnInit {
  cart: Cart;
  isLoading: boolean = false;
  result: SweetAlertResult<any>;
  inputValue:number;
  constructor(private cartSharedService: CartSharedService, private router: Router, private cartService: CartService, private swlAlerts: SwlAlerts) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.swlAlerts.showLoandingModal("Cargando libros...");
    AuthService.isClient().subscribe((isClient: boolean) => {
      if (!isClient) {
        this.router.navigate(['/']);
      } else {
        this.loadCart();
        this.isLoading = false;
      }
    });
  }

  loadCart(): void {
    this.cartService.getCart().subscribe(
      (cart: Cart) => {
        this.cart = cart;
        this.cart.total = parseFloat(
          (Math.round((cart.amount + Cart.shipping) * 100) / 100).toFixed(2)
        );
        Swal.close();
        this.cartSharedService.updateCart(this.cart);
      },
      (error) => {

      }
    );
  }

  async removeFromCart(isbn: string, selectedQuantity:number) {
    if (selectedQuantity == 1) {
       this.result = await this.swlAlerts.alertConfirmation('¿Estás seguro?',"¡No podrás revertir esto!");
    }
    if (this.result.isConfirmed) {
      this.cartService.removeFromCart(isbn).subscribe(
        () => {

          this.loadCart();
        },
        (error) => {
          this.swlAlerts.alertError("¡Error eliminando el!");
        }
      );
      Swal.fire({
        title: "¡Borrado!",
        text: "El libro ha sido eliminado del carrito",
        icon: "success"
      });
    }

  }
  addItemToCart(isbn: string, quantity: number): void {
    this.swlAlerts.showLoandingModal("Añadiendo libro...");
    this.cartService.addItemToCart(isbn, quantity).subscribe(
      () => {
        this.loadCart();
      },
      (error) => {
        this.swlAlerts.alertError("¡Sin Stock!");
      }
    );
  }

  updateQuantity(isbn: string, quantity: number): void {
    this.swlAlerts.showLoandingModal("¡Actualizando...!");
    this.cartService.updateCartBookQuantity(isbn, quantity).subscribe(
      () => {
        this.loadCart();
      },
      (error) => {
        this.swlAlerts.alertError("¡Ha habido un error!")
      }
    );
  }

  clearCart(): void {
    this.swlAlerts.showLoandingModal("¡Vaciando el carrito...!");
    this.cartService.clearCart().subscribe(
      () => {
        this.loadCart();
      },
      (error) => {
        this.swlAlerts.alertError("Habido un error");

      }
    );
  }


  bookMap(isbn: string, quantity: number){
    this.cartService.addItemToCart(isbn, quantity).subscribe({
      next: (response: any) => {
        this.cart = response.content.map((book: { id: any; }) => ({
          ...book,
          viewTransitionName: `book-detail-${book.id}`
        }));
      }
    });
}

}
