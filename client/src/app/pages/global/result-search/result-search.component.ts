import { Component, OnInit } from '@angular/core';
import { SendFiltersService } from '../../../core/services/send-filters.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Book } from '../../../core/models/Book';
import { BookSearchCriteria } from '../../../core/models/book-search-criteria';
import { Genres } from '../../../core/models/Genres';
import { Pagination } from '../../../core/models/sharedEntities/pagination';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';
import { BookService } from '../../../core/services/book/book-service';
import { FavouritesService } from '../../../core/services/favourites/favourites.service';
import { StarsComponent } from '@components/stars/stars.component';
import { CartService } from '@services/cart/cart.service';
import { SwlAlerts } from '@utils/swl';

@Component({
  selector: 'app-result-search',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule, StarsComponent],
  providers: [],
  templateUrl: './result-search.component.html',
  styleUrl: './result-search.component.css',
})
export class ResultSearchComponent implements OnInit {
  books: Book[] = [];

  //para la busqueda avanazada
  bookSearch: BookSearchCriteria = new BookSearchCriteria();
  genreString: string = '';
  isClient: boolean = false;
  isSeller: boolean = false;
  isAdmin: boolean = false;

  //datos recibidos de la pagina principal
  //filterText: string = '';
  genres: Genres[] = [];

  //paginacion
  pagination: Pagination = new Pagination();
  pages: number[] = [];
  genresString:string;

  constructor(
    private filterService: SendFiltersService,
    private bookService: BookService,
    private router: Router,
    private favouritesService: FavouritesService,
    private swlAlerts: SwlAlerts,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.pagination.pageNumber = 0;
    this.pagination.pageSize = 5;

    AuthService.isSeller().subscribe((isSeller: boolean) => {
      if (isSeller === true) {
        this.isSeller = true;
      }
    });

    AuthService.isAdmin().subscribe((isAdmin: boolean) => {
      if (isAdmin === true) {
        this.isAdmin = true;
      }
    });
    //obtiene array de generos
    this.bookService.getAllGenre().subscribe((genres: Genres[]) => {
      this.genres = genres;
    });
    //fin

    //filtrado inicial
    console.log('el filtro es' + this.filterService.getFilter());
    switch (this.filterService.getFilter()) {
      case 'title':
        this.bookSearch.title = this.filterService.getFilterText();
        console.log('Opción 1 seleccionada' + this.bookSearch.title);
        break;
      case 'author':
        this.bookSearch.authorFirstName = this.filterService.getFilterText();
        console.log('Opción 2 seleccionada' + this.bookSearch.authorFirstName);
        break;
      case 'isbn':
        //está por hacer
        this.bookSearch.isbn = this.filterService.getFilterText();
        console.log('Opción 3 seleccionada');
        break;
    }

    console.log(this.bookSearch);
    this.getFilterBooks(
      this.pagination.pageNumber,
      this.pagination.pageSize,
      this.bookSearch
    );
    //fin de filtro

    console.log('esto se obtiene despues del filtrado inicial' + this.books);
  }

  onSelectBooks(event: any) {
    const pageSize = event.target.value;
    this.pagination.pageSize = pageSize;
    this.pagination.pageNumber = 0;
    this.getFilterBooks(
      this.pagination.pageNumber,
      this.pagination.pageSize,
      this.bookSearch
    );
  }

  getFilterBooks(s: number, n: number, bookSearch: BookSearchCriteria) {
    this.books = [];
    if(this.genreString!=""){
      this.bookSearch.genres=this.getGenres();
    }
    
    this.bookService
      .getFilterBooks(s, n, bookSearch)
      .subscribe((response: any) => {
        window.scrollTo(0, 1000);
        this.books = response.content;
        if (sessionStorage.getItem('ACCESS_TOKEN') != null) {
          AuthService.isClient().subscribe((isClient: boolean) => {
            if (isClient === true) {
              this.isClient = true;
              this.favouritesService
                .getUserFavs(String(s), String(n))
                .subscribe((favourites: any) => {
                  favourites.content.forEach((fav: any) => {
                    this.books.forEach((book: any) => {
                      if (fav.isbn === book.isbn) {
                        book.isFavourite = true;
                      }
                    });
                  });
                });
            }
          });
        }
        this.pagination.totalElements = response.totalElements;
        this.pagination.totalPages = response.totalPages;
        this.pagination.first = response.first;
        this.pagination.last = response.last;
        this.bookSearch.genres=[];

        this.pages = [];
        for (let i = 0; i < this.pagination.totalPages; i++) {
          this.pages[i] = i + 1;
        }

        console.log('estas son las paginas en filterBooks' + this.pages);
        console.log(this.pagination.pageNumber);
      });
  }

  addToFavorites(isbn: string) {
    if (sessionStorage.getItem('ACCESS_TOKEN') == null) {
      this.swlAlerts
        .alertConfirmationWithDeny(
          'Login',
          'Debes iniciar sesión o registrarte para añadir a favoritos'
        )
        .then((result) => {
          if (result.isConfirmed) {
            this.router.navigate(['login']);
          } else if (result.isDenied) {
            this.router.navigate(['register']);
          } else if (result.isDismissed) {
            return;
          }
        });
    }
    AuthService.isClient().subscribe((isClient: boolean) => {
      if (isClient === true) {
        this.favouritesService.addFavourite(isbn).subscribe({
          next: (response: any) => {
            for (const element of this.books) {
              if (element.isbn === isbn) {
                element.isFavourite = true;
              }
            }
            console.log('Añadir a favoritos');
          },
          error: (error) => {
            console.error('Error adding favorite:', error);
          },
        });
      } else {
        this.router.navigate(['login']);
      }
    });
  }

  removeFavourite(isbn: string) {
    if (sessionStorage.getItem('ACCESS_TOKEN') == null) {
      this.swlAlerts
        .alertConfirmationWithDeny(
          'Login',
          'Debes iniciar sesión o registrarte para eliminar de favoritos'
        )
        .then((result) => {
          if (result.isConfirmed) {
            this.router.navigate(['login']);
          } else if (result.isDenied) {
            this.router.navigate(['register']);
          } else if (result.isDismissed) {
            return;
          }
        });
    }
    AuthService.isClient().subscribe((isClient: boolean) => {
      if (isClient === true) {
        this.favouritesService.deleteFavourite(isbn).subscribe({
          next: (response: any) => {
            for (const element of this.books) {
              if (element.isbn === isbn) {
                element.isFavourite = false;
              }
            }
            console.log('Añadir a favoritos');
          },
          error: (error) => {
            console.error('Error adding favorite:', error);
          },
        });
      } else {
        this.router.navigate(['login']);
      }
    });
  }

  addToCart(book: Book) {
    if (sessionStorage.getItem('ACCESS_TOKEN') == null) {
      this.swlAlerts
        .alertConfirmationWithDeny(
          'Login',
          'Debes iniciar sesión o registrarte para añadir al carrito'
        )
        .then((result) => {
          if (result.isConfirmed) {
            this.router.navigate(['login']);
          } else if (result.isDenied) {
            this.router.navigate(['register']);
          } else if (result.isDismissed) {
            return;
          }
        });
    }
    AuthService.isClient().subscribe((isClient: boolean) => {
      if (isClient === true) {
        //Añadir el carrito
        this.cartService.addItemToCart(book.isbn, 1).subscribe(
          () => {
            console.log('The book is added: ', book);
            this.swlAlerts.showToastSuccess(
              'El libro ' + book.title + ' se ha añadido al carrito'
            );
            this.books.forEach((b) => {
              if (b.isbn === book.isbn) {
                b.stock--;
              }
            });
            book.isLoading = false;
          },
          (error) => {
            console.error('Error adding item to cart:', error);
            if (error.status === 400) {
              this.swlAlerts.showToastError(
                'No hay stock disponible para este libro'
              );
            } else {
              this.swlAlerts.showToastError(
                'Error al añadir el libro al carrito'
              );
            }
            book.isLoading = false;
          }
        );
      } else {
        this.router.navigate(['login']);
      }
    });
  }
  //filtrar
  doFilter(): void {
    console.log(
      'valores ' + this.pagination.pageNumber + ' ' + this.pagination.pageSize
    );

    if (this.genreString != '') {
      this.genreStringToArray();
    }

    this.getFilterBooks(
      this.pagination.pageNumber,
      this.pagination.pageSize,
      this.bookSearch
    );
  }

  //cambiar página
  changePage(nPage: number) {
    console.log('el parametro es ' + nPage);
    if (nPage >= this.pagination.totalPages) {
      nPage = this.pagination.totalPages - 1;
    } else if (nPage < 0) {
      nPage = 0;
    } else {
      this.pagination.pageNumber = nPage;
    }
    this.getFilterBooks(
      this.pagination.pageNumber,
      this.pagination.pageSize,
      this.bookSearch
    );
  }

  coverIsEmpty(book: Book): string {
    if (book.cover === null) {
      return 'assets/images/noCover.jpg';
    }
    return book.cover;
  }

  goToDetails(isbn: string) {
    this.filterService.setIsbn(isbn);
    this.router.navigate(['book/details/' + isbn]);
  }

  genreStringToArray(): void {
    let genres: string[] = this.genreString.split(' ');
    this.bookSearch.genres = genres;

    console.log(this.bookSearch.genres);
  }

  //divide el string en un array cuando hay un espacio o varios
  getGenres():string[]{
    console.log("esta es la salida del string"+this.genreString);
    let array:string[]=[];
    array=this.genreString.split(/\s+/);

    return array;
  }

}
