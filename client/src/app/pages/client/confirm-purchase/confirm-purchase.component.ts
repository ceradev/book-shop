import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';

@Component({
  selector: 'app-confirm-purchase',
  standalone: true,
  templateUrl: './confirm-purchase.component.html',
  styleUrl: './confirm-purchase.component.css'
})
export class ConfirmPurchaseComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
    AuthService.isClient().subscribe((isClient: boolean) => {
      if(!isClient){
        this.router.navigate(['/']);
      }
    })
  }
  mensajeCabecera:string="Gracias por su compra."
  referencia:string="A2342394";
  mensajeConfirmacion:string="Su pedido con referencia "+this.referencia+" ha sido gestionado con éxito."
  confirmacionRutaImagen:string="/assets/images/confirmado.png"
  NoConfirmacionRutaImagen:string="/assets/images/cancelado.png"
  mensajeDespedida:string="Recibirá un correo electrónico con los datos de compra"
  exito:boolean=false;
}
