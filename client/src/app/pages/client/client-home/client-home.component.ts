import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CartService } from '@services/cart/cart.service';
import { Pagination } from '@models/sharedEntities/pagination';
import { Book } from '@models/Book';
import { BookService } from '@services/book/book-service';
import { SendFiltersService } from '@services/send-filters.service';
import { AuthService } from '@services/auth/auth.service';
import { BookSearchCriteria } from '../../../core/models/book-search-criteria';
import { FavouritesService } from '../../../core/services/favourites/favourites.service';
import { SwlAlerts } from '../../../shared/utils/swl';
import { StarsComponent } from '@components/stars/stars.component';

@Component({
  selector: 'app-client-home',
  standalone: true,
  templateUrl: './client-home.component.html',
  styleUrl: './client-home.component.css',
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    RouterModule,
    StarsComponent,
  ],
})
export class ClientHomeComponent implements OnInit {
  books: Book[] = [];
  filterText: string = '';
  select: string = '';
  pagination: Pagination = new Pagination();
  pageSizeFav: number = 50;
  pageNumberFav: number = 0;
  pages: number[] = [];
  isClient: boolean = false;
  isSeller: boolean = false;
  isAdmin: boolean = false;
  bookSearch: BookSearchCriteria = new BookSearchCriteria();

  //para cambiar el texto al hacer búsqueda
  searchResults: boolean;

  constructor(
    private bookService: BookService,
    private filterService: SendFiltersService,
    private router: Router,
    private cartService: CartService,
    private favouritesService: FavouritesService,
    private swlAlerts: SwlAlerts
  ) {}

  ngOnInit(): void {
    this.pagination.pageNumber = 0;
    this.pagination.pageSize = 5;

    if (sessionStorage.getItem('ACCESS_TOKEN') != null) {
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
    }
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
    this.bookMap(this.pagination.pageNumber, this.pagination.pageSize);
  }

  getBooks(s: number, n: number) {
    this.bookService.getAllBooks(String(s), String(n)).subscribe(
      (response: any) => {
        this.books = response.content;
        if (sessionStorage.getItem('ACCESS_TOKEN') != null) {
          AuthService.isClient().subscribe((isClient: boolean) => {
            if (isClient === true) {
              this.isClient = isClient;
              this.favouritesService
                .getUserFavs(String(s), String(n))
                .subscribe((favourites: any) => {
                  favourites.content.forEach((fav: any) => {
                    this.books.forEach((book: any) => {
                      book.isFavourite = false;
                      if (fav.isbn === book.isbn) {
                        book.isFavourite = true;
                        console.log(book);
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
        this.pages = [];
        for (let i = 0; i < this.pagination.totalPages; i++) {
          this.pages[i] = i + 1;
        }
      },
      (error) => {
        console.log(error);
      }
    );
  }

  onSelectBooks(event: any) {
    const pageSize = event.target.value;
    this.pagination.pageSize = pageSize;
    this.pagination.pageNumber = 0;
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
  }

  coverIsEmpty(book: Book): string {
    if (book.cover === null) {
      return 'assets/images/noCover.jpg';
    }
    return book.cover;
  }

  doSearch() {
    console.log(this.select + ':' + this.filterText);
    this.filterService.setFilter(this.select);
    this.filterService.setFilterText(this.filterText);

    console.log(
      this.filterService.getFilter + ' ' + this.filterService.getFilterText
    );
    this.router.navigate(['search']);
  }

  addToFavorites(isbn: string) {
    if (sessionStorage.getItem('ACCESS_TOKEN') == null) {
      this.swlAlerts
        .alertConfirmationWithDeny(
          'Aviso',
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
                this.swlAlerts.showToastSuccess(
                  'El libro ' + element.title + ' se ha añadido a favoritos'
                );
              }
            }
          },
          error: (error) => {
            console.error('Error adding favorite:', error);
            this.swlAlerts.showToastError(
              'Error al añadir el libro a favoritos'
            );
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
          'Debes iniciar sesión o registrarte para añadir a favoritos'
        )
        .then((result) => {
          if (result.isConfirmed) {
            this.router.navigate(['login']);
          } else if (result.isDenied) {
            this.router.navigate(['register']);
          } else if (result.isDismissed) {
            this.router.navigate(['home']);
          }
        });
      return;
    }
    AuthService.isClient().subscribe((isClient: boolean) => {
      if (isClient === true) {
        this.favouritesService.deleteFavourite(isbn).subscribe({
          next: (response: any) => {
            for (const element of this.books) {
              if (element.isbn === isbn) {
                element.isFavourite = false;
                this.swlAlerts.showToastSuccess(
                  'El libro ' + element.title + ' se ha quitado de favoritos'
                );
              }
            }
            console.log('Añadir a favoritos');
          },
          error: (error) => {
            console.error('Error adding favorite:', error);
            this.swlAlerts.showToastError(
              'Error al quitar el libro de favoritos'
            );
          },
        });
      } else {
        this.router.navigate(['login']);
      }
    });
  }

  addToCart(book: Book) {
    book.isLoading = true;
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
                'No hay suficientes libros en stock'
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

  // Método para manejar cambios en la selección
  selectOption(opcion: string) {
    this.select = opcion;
    console.log(this.select);
  }

  changePage(nPage: number) {
    if (nPage >= this.pagination.totalPages) {
      nPage = this.pagination.totalPages - 1;
    } else if (nPage < 0) {
      nPage = 0;
    }
    this.pagination.pageNumber = nPage;
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
  }
  goToDetails(isbn: string) {
    this.filterService.setIsbn(isbn);
    this.router.navigate(['book/details/' + isbn]);
  }

  onInputChange(event: any): void {
    this.filterText = event.target.value;
    this.pagination.pageNumber = 0;
    this.searching(
      this.pagination.pageNumber,
      this.pagination.pageSize,
      this.filterText
    );
  }

  searching(s: number, n: number, query: string) {
    if (this.filterText === '') {
      this.getBooks(this.pagination.pageSize, this.pagination.pageNumber);
      return;
    }
    this.bookService
      .searchBooksByQueryString(query, s, n)
      .subscribe((response: any) => {
        this.books = response.content;
        if (sessionStorage.getItem('ACCESS_TOKEN') != null) {
          AuthService.isClient().subscribe((isClient: boolean) => {
            if (isClient === true) {
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

        this.pages = [];
        for (let i = 0; i < this.pagination.totalPages; i++) {
          this.pages[i] = i + 1;
        }

        console.log(this.books);
      });
  }

  goToAdvancedSearch() {
    this.router.navigate(['/search']);
  }

  isEmptyInput(): boolean {
    return this.filterText.trim().length === 0;
  }

  bookMap(s: number, n: number) {
    this.bookService.getAllBooks(String(s), String(n)).subscribe({
      next: (response: any) => {
        this.books = response.content.map((book: { id: any }) => ({
          ...book,
          viewTransitionName: `book-detail-${book.id}`,
        }));
      },
    });
  }
}
