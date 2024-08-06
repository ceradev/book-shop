import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Book } from '../../../core/models/Book';
import { Pagination } from '../../../core/models/sharedEntities/pagination';
import { FavouritesService } from '../../../core/services/favourites/favourites.service';
import { Favourites } from '../../../core/models/Favourites';
import { Router, RouterModule } from '@angular/router';
import { map } from 'rxjs';
import { AuthService } from '../../../core/services/auth/auth.service';
import { SwlAlerts } from '../../../shared/utils/swl';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-favourites',
  standalone: true,
  imports: [CommonModule, FormsModule, MatFormFieldModule, RouterModule],
  templateUrl: './favourites.component.html',
  styleUrl: './favourites.component.css',
})
export class FavouritesComponent implements OnInit {
  books: Book[] = [];

  //paginacion
  pagination: Pagination = new Pagination();
  pages: number[] = [];
  isLoading: boolean = false;
  //favorito
  favs: Favourites[] = [];

  constructor(
    private favouritesService: FavouritesService,
    private authService: AuthService,
    private router: Router,
    private swlAlerts: SwlAlerts
  ) {}

  ngOnInit(): void {
    this.pagination.pageNumber = 0;
    this.pagination.pageSize = 5;
    this.isLoading = true;
    this.swlAlerts.showLoandingModal('Cargando tu lista de favoritos...');
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
    this.isLoading = false;
  }

  getBooks(s: number, n: number) {
    this.favouritesService.getUserFavs(String(s), String(n)).subscribe({
      next: (response: any) => {
        this.books = response.content;
        this.pagination.totalElements = response.totalElements;
        this.pagination.totalPages = response.totalPages;
        this.pagination.first = response.first;
        this.pagination.last = response.last;

        console.log(this.pagination);
        this.pages = [];
        for (let i = 0; i < this.pagination.totalPages; i++) {
          this.pages[i] = i + 1;
        }

        Swal.close();

        this.books.forEach((book) => {
          const favourite = new Favourites();
          favourite.setBookIsbn(book.isbn);
          favourite.setIsFav(true);
          this.favs.push(favourite);
        });
      },
      error: (error) => {
        console.error('Error fetching books:', error);
      },
    });
  }

  deleteFavourite(isbn: string) {
    this.favouritesService.deleteFavourite(isbn).subscribe({
      next: (response: any) => {
        console.log(response);

        // Borrar del array de libros favoritos
        const index = this.books.findIndex((book) => book.isbn === isbn);
        if (index !== -1) {
          this.books.splice(index, 1);
          this.swlAlerts.showToastSuccess('El libro con ISBN ' + isbn + ' ha sido quitado de favoritos');
        }
      },
      error: (error) => {
        console.error('Error deleting favorite:', error);
      },
    });
  }
  setFavourite(isbn: string) {
    return this.favs.find((book) => book.bookIsbn === isbn);
  }

  changeFavourite(isbn: string, isFav: boolean) {
    const index = this.favs.findIndex((book) => book.bookIsbn === isbn);
    this.favs[index].setIsFav(isFav);
  }

  coverIsEmpty(book: Book): string {
    if (book.cover === null) {
      return 'assets/images/noCover.jpg';
    }
    return book.cover;
  }

  changePage(nPage: number) {
    console.log('el parametro es ' + nPage);

    //si es menor que la pagina actual y mayor que 0
    if (nPage < this.pagination.pageNumber && nPage >= 0) {
      this.pagination.pageNumber = this.pagination.pageNumber - 1;
    }

    //si es mayor que la página actual y menor que el número total de páginas
    if (
      nPage > this.pagination.pageNumber &&
      nPage < this.pagination.totalPages
    ) {
      this.pagination.pageNumber = this.pagination.pageNumber + 1;
    }

    console.log('pagina actual' + this.pagination.pageNumber);

    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
  }

  addToCart(book: Book) {
    this.authService
      .isAuthenticated()
      .pipe(
        map((isAuthenticated: boolean) => {
          if (isAuthenticated) {
            console.log('Añadir al carrito');
          } else {
            this.router.navigate(['login']);
          }
        })
      )
      .subscribe();
  }

  onSelectBooks(event: any) {
    const pageSize = event.target.value;
    this.pagination.pageSize = pageSize;
    this.pagination.pageNumber = 0;
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
  }

  bookMap(s:number, n: number){
    this.favouritesService.getUserFavs(String(s), String(n)).subscribe({
      next: (response: any) => {
        this.books = response.content.map((book: { id: any; }) => ({
          ...book,
          viewTransitionName: `view-transition-name: book-detail-${book.id}`
        }));
  }})
}

}
