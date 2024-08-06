import { Component, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { Cart } from '@models/Carts';
import { PaypalService } from '@services/paypay/paypal.service';
import { CartSharedService } from '@shared/services/cart/cart-shared.service';
import { NgxPayPalModule, IPayPalConfig, ICreateOrderRequest } from 'ngx-paypal';

@Component({
  selector: 'app-payme',
  standalone: true,
  imports: [NgxPayPalModule],
  templateUrl: './payme.component.html',
  styleUrls: ['./payme.component.css']
})
export class PaymeComponent {
  //@ViewChild('paypal', { static: true }) paypalElement: ElementRef;
  public payPalConfig ? : IPayPalConfig;
  cart: Cart;
  paidFor = false;
  showCancel: boolean;
  showSuccess: boolean;
  showError: boolean;
  orderID :any;

  constructor(private payPalService: PaypalService,private cartSharedService: CartSharedService,) {}

  ngAfterViewInit() {
        this.cartSharedService.cart$.subscribe(cart => {
          this.cart = cart;
        });
      }
  ngOnInit(): void {
    this.initConfig();

  }

  private initConfig(): void {
    this.payPalConfig = {
        currency: 'EUR',
        fundingSource : 'PAYPAL',
        clientId: 'AU9d5Oz60cf-hup6rsKAJzBPTeTucwI3b_6mDUyVswQUxvb7-hCWB_X0gL5OPovn2p8drV2lVWob3PRT',
        createOrderOnServer: (data :any) => this.payPalService.createOrder(this.cart.id).toPromise().then(
          (order) => {
            console.log(order)
            return order.orderId;
          },
          (error) => {
            console.error('Error creating order:', error);
          }
        ),
        onApprove: (data, actions) => {
            console.log('onApprove - transaction was approved, but not authorized', data, actions);

            actions.order.get().then((details: any) => {
                console.log('onApprove - you can get full order details inside onApprove: ', details);
            });

        },
        onClientAuthorization: (data) => {
            console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
            this.payPalService.completeOrder(data.id).toPromise().then(
              (response) => {
                console.log(response.status)
              },
              (error) => {
                console.error('Error creating order:', error);
              }
            )
            this.showSuccess = true;
        },
        onCancel: (data, actions) => {
            console.log('OnCancel', data, actions);
            this.showCancel = true;

        },
        onError: err => {
            console.log('OnError', err);
            this.showError = true;
        },
        onClick: (data, actions) => {
            console.log('onClick', data, actions);
            this.resetStatus();
        },
    };
}
resetStatus() {
  this.paidFor = true;
}
}































