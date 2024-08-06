import { CommonModule, NgIf } from '@angular/common';
import { Component, Input, NgModule, OnInit } from '@angular/core';
import {MdbTooltipModule } from 'mdb-angular-ui-kit/tooltip';
import { BookService } from '../../../core/services/book/book-service';
import { Book } from '../../../core/models/Book';
import { FormsModule, } from '@angular/forms';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { Genres } from '../../../core/models/Genres';
import { BookData } from '../../../core/models/book-data';
import { AuthService } from '../../../core/services/auth/auth.service';





@Component({
  selector: 'app-add-book',
  standalone: true,
  imports: [MdbTooltipModule, NgIf, FormsModule, HttpClientModule, CommonModule],
  templateUrl: './add-book.component.html',
  styleUrl: './add-book.component.css'
})
export class AddBookComponent implements OnInit{

  bookData:BookData=new BookData();
  book= {
    title: '',
    isbn: '',
    authorFirstName: '',
    authorLastName: '',
    authorId: 0,
    editorial: '',
    genre: [],
    synopsis: '',
    edition: '',
    price: '',
    publishDate: new Date(),
    stock: 0,
  };
  genres:Genres[]=[];
  seller:string="b34f5471-372b-4fea-acac-d900700a721e";

  constructor(private bookService:BookService, private router:Router){}
  
  
  ngOnInit(): void {

    AuthService.isSeller().subscribe((isSeller: boolean) => {
      if(!isSeller){
        this.router.navigate(['/']);
      }
    });

    this.bookService.getAllGenre().subscribe(
     
      (genres: Genres[]) => {
        this.genres=genres;        
      }
    );

  }

  //aqui es donde guardaremos el archivo
  file: File = new File([],'');
  imageUrl: string | null = null;


  //al pulsar el boton de seleccionar imagen
  onFileSelected(event: any) {
    //añade el 
    this.file = event.target.files[0];
    

    this.createImageFromBlob(this.file);
  }


  //para previsualizar la imagen
  createImageFromBlob(image: File) {
    const reader = new FileReader();
    reader.onload = (event: any) => {
      this.imageUrl = event.target.result;
    };
    reader.readAsDataURL(image);
  }


  //para el ngIf
  imageExist(): boolean {
    return this.imageUrl !== null;
  }



  //añadir libro
  newBook():void{

    this.bookData.seller=this.seller;

    console.log(JSON.stringify(this.bookData));

      this.bookService.createBook(this.bookData, this.file).subscribe(response => 
        {
          //this.router.navigate(['']);
          window.location.reload();
          
          console.log(response);
        }
      );
    
      swal.fire("Nuevo libro añadido: ", `Titulo: ${this.bookData.title} con ISBN: ${this.bookData.isbn}`, 'success');
  }
}
