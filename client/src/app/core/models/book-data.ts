export class BookData {

  isbn:string; 
  title:string; 
  authorFirstName:string;
  authorLastName:string;
  authorId:number; 
  editorial:string; 
  genre:number[];
  synopsis:string;
  edition:string;
  price:string;
  publishDate:Date; // Año, Mes (0-11), Día
  stock:number;
  seller:string;


  constructor(){
    this.genre=[];
  }
  

}


