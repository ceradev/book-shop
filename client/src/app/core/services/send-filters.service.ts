import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SendFiltersService {

  //datos a enviar a pagina de busqueda
  private filterText:string="";
  private filter:string="";

  //para el envio del isbn a la vista de detalles libro
  private isbn:string="";




  constructor() { }


  setFilterText(text:string){
    this.filterText=text;
    
  }

  setFilter(filter:string){
    this.filter=filter;
  }


  getFilterText():string{
    return this.filterText;
  }
  
  getFilter():string{
    return this.filter;
  }




  setIsbn(isbn:string){
    this.isbn=isbn;
  }

  getIsbn(){
    return this.isbn;
  }

  

}
