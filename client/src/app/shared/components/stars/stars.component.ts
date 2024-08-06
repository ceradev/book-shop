import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-stars',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './stars.component.html',
  styleUrl: './stars.component.css'
})
export class StarsComponent  implements OnInit{
  @Input() nota: any;

  estrellasArray:number[]=[];
  estrellasVaciasArray:number[]=[];
  estrellasMitadArray:number[]=[];
  decimal:number | undefined;
  
  ngOnInit(): void {
    this.calcular(this.nota);


  	
  }


  calcular(numero:number):void{

    this.decimal= numero%1;

    if(this.decimal!=0){
      if(this.decimal>0.2 && this.decimal<0.8){
        this.estrellasMitadArray[0]=1;
      }else if(this.decimal>=0.8){
        numero=Math.round(numero);
      }else{
        numero=Math.floor(numero);
      }
      
    }

    this.estrellasArray=Array.from({length:numero},
      (_, index) => index + 1);


    this.estrellasVaciasArray=Array.from({length:(5-(numero+this.estrellasVaciasArray.length))},
    (_, index) => index + 1);

}
}
