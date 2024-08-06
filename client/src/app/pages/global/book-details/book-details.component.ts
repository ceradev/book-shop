import { Component, OnInit } from '@angular/core';
import { StarsComponent } from '../../../shared/components/stars/stars.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule, NgFor } from '@angular/common';
import { BookDetails } from '../../../core/models/book-details';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewsByIsbn } from '../../../core/models/Reviews';
import { ReviewService } from '../../../core/services/review/review.service';
import { ReviewPost } from '../../../core/models/review-post';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../../core/services/book/book-service';
import { FavouritesService } from '../../../core/services/favourites/favourites.service';
import { AuthService } from '../../../core/services/auth/auth.service';
import { jwtDecode } from 'jwt-decode';
import { SwlAlerts } from '../../../shared/utils/swl';
import { CartService } from '@services/cart/cart.service';

@Component({
  selector: 'app-book-details',
  standalone: true,
  imports: [
    StarsComponent,
    MatDialogModule,
    MatButtonModule,
    NgFor,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './book-details.component.html',
  styleUrl: './book-details.component.css',
})
export class BookDetailsComponent implements OnInit {
  barWidths: number[] = [0, 0, 0, 0, 0];
  pageSizeFav: number = 50;
  pageNumberFav: number = 0;
  isSeller: boolean = false;
  isAdmin: boolean = false;
  isClient: boolean = false;

  isbn: string;
  book: BookDetails = new BookDetails();
  authorName: string = '';

  reviews: ReviewsByIsbn[] = [];
  newReview: ReviewPost = new ReviewPost();

  //variables de control
  isReviewOpen: boolean = false; //controla si se abre la zona de review
  userPostedReview: boolean = false; //controla si el usuario tiene review
  userModReview: boolean = false; //controla si el usuario modifica review

  constructor(
    private bookService: BookService,
    private activateRoute: ActivatedRoute,
    private reviewService: ReviewService,
    private favouritesService: FavouritesService,
    private router: Router,
    private swlAlerts: SwlAlerts,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    window.scrollTo(0, 0);

    //recoge el parametro que se pasa por id
    this.activateRoute.params.subscribe((params) => {
      this.isbn = params['id'];
    });

    this.newReview.rating = 0;
    this.getBookByIsbn(this.isbn);
    this.getReviewsByIsbn(this.isbn);

    if (sessionStorage.getItem('ACCESS_TOKEN') != null) {
      AuthService.isClient().subscribe((isClient: boolean) => {
        if (isClient === true) {
          this.isClient = true;
        }
      });

      AuthService.isAdmin().subscribe((isAdmin: boolean) => {
        if (isAdmin === true) {
          this.isAdmin = true;
        }
      });

      AuthService.isSeller().subscribe((isSeller: boolean) => {
        if (isSeller === true) {
          this.isSeller = true;
        }
      });
    }
  }

  //MÉTODOS PARA FAVORITOS
  addToFavourites(isbn: string) {
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
            this.book.isFavourite = true;
            this.swlAlerts.showToastSuccess(
              'El libro ha sido añadido a favoritos'
            );
          },
          error: (error) => {
            console.error('Error adding favorite:', error);
            this.swlAlerts.showToastError('No se ha podido añadir a favoritos');
          },
        });
      }
    });
  }

  //recoge el parametro que se pasa por id
  addToCart(isbn: string) {
    if (sessionStorage.getItem('ACCESS_TOKEN') == null) {
      this.swlAlerts
        .alertConfirmationWithDeny(
          'Login',
          'Debes iniciar sesión o registrarte para añadir el libro al carrito'
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
        this.cartService.addItemToCart(isbn, 1).subscribe(
          () => {
            this.swlAlerts.showToastSuccess(
              'El libro se ha añadido al carrito'
            );
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
          }
        );
      } else {
        this.router.navigate(['login']);
      }
    });
  }

  removeFavourite(isbn: string) {
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
        this.favouritesService.deleteFavourite(isbn).subscribe({
          next: (response: any) => {
            this.book.isFavourite = false;
          },
          error: (error) => {
            console.error('Error adding favorite:', error);
            this.swlAlerts.showToastError('No se ha podido eliminar el libro de favoritos');
          },
        });
      }
    });
  }

  //FIN DE MÉTODOS PARA FAVORITOS

  coverIsEmpty(book: BookDetails): string {
    if (book.cover === null) {
      return 'assets/images/noCover.jpg';
    }
    return book.cover;
  }

  getBookByIsbn(isbn: string) {
    this.bookService.getBookById(isbn).subscribe((response) => {
      this.book.reviewMean = 0;
      this.book = response;
      this.authorName =
        response.author.firstName + ' ' + response.author.lastName;

      if (sessionStorage.getItem('ACCESS_TOKEN') === null) {
        return;
      }
      AuthService.isClient().subscribe((isClient: boolean) => {
        if (isClient === true) {
          this.favouritesService
            .getUserFavs(String(this.pageNumberFav), String(this.pageSizeFav))
            .subscribe((favourites: any) => {
              favourites.content.forEach((fav: any) => {
                if (fav.isbn === isbn) {
                  this.book.isFavourite = true;
                }
              });
            });
        }
      });
    });
  }

  getUserId() {
    const token = sessionStorage.getItem('ACCESS_TOKEN');
    if (token) {
      const decodeToken: any = jwtDecode(token);
      return decodeToken.sub || null;
    }
    return null;
  }

  //METODOS PARA REVIEWS

  generateReview(): void {
    this.newReview.isPurchased = false;
    this.newReview.bookIsbn = this.isbn;
    this.validateRating();
  }

  getReviewsByIsbn(isbn: string) {
    let reviewsNoteGraph: number[] = [0, 0, 0, 0, 0, 0]; //estrellas: total, 1, 2, 3, 4, 5
    this.reviewService.getAllReviewByIsbn(isbn).subscribe((response) => {
      this.reviews = response;

      this.reviews.forEach((review) => {
        let index: number = review.rating;
        reviewsNoteGraph[index] = reviewsNoteGraph[review.rating] + 1;
        reviewsNoteGraph[0] = reviewsNoteGraph[0] + 1;
      });

      for (let i = 1; i < reviewsNoteGraph.length; i++) {
        this.barWidths[i - 1] =
          (reviewsNoteGraph[i] / reviewsNoteGraph[0]) * 100;
      }

      //reordeno las reviews
      this.reorderReviews();
    });
  }

  modReview() {
    if (sessionStorage.getItem('ACCESS_TOKEN') == null) {
      this.swlAlerts
        .alertConfirmationWithDeny(
          'Login',
          'Debes iniciar sesión o registrarte para actualizar tus reviews',
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

    if (this.reviews[0].user.id === this.getUserId()) {
      this.newReview.id = this.reviews[0].id;

      AuthService.isClient().subscribe((isClient: boolean) => {
        if (isClient === true) {
          this.generateReview();
          this.reviewService
            .putReview(this.newReview)
            .subscribe((response: any) => {
              this.newReview = response;
              this.getBookByIsbn(this.isbn);
              this.swlAlerts.showToastSuccess('Su reseña ha sido actualizada');
              this.getReviewsByIsbn(this.isbn);
              this.userModRev();

              //obtengo el libro
              this.getBookByIsbn(this.isbn);
            });
        }
      });
    } else {
      this.swlAlerts.showToastError('No tienes permiso para modificar esta reseña');
    }
  }

  saveReview() {
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
        this.generateReview();

        this.reviewService
          .postReview(this.newReview)
          .subscribe((response: any) => {
            this.newReview = response;
            this.getReviewsByIsbn(this.isbn);
            this.swlAlerts.showToastSuccess('Su reseña ha sido publicada');
            this.reorderReviews();
            this.userPostedReview = true;
            this.swlAlerts.showToastSuccess('Su reseña ha sido publicada');
            //obtengo el libro
            this.getBookByIsbn(this.isbn);
          });
      } else {
        this.swlAlerts.showToastError('No tienes permisos para publicar una reseña');
      }
    });
  }

  //reordena reviews poniendo la primera la del usuario, si existe
  reorderReviews(): void {
    //recorro el array de reviews
    const user: any = this.getUserId();

    if (user == null) {
      return;
    }

    for (let i = 0; i < this.reviews.length; i++) {
      if (this.reviews[i].user.id === user) {
        //hacemos true que el usuario ha publicado review
        this.userPostedReview = true;
        //ponemos la primera la review del usuario
        let temp: ReviewsByIsbn = this.reviews[0];
        this.reviews[0] = this.reviews[i];
        this.reviews[i] = temp;

        //asigno la review a newReview
        this.newReview.userId = this.reviews[0].user.id;
        this.newReview.comment = this.reviews[0].comment;
        this.newReview.rating = this.reviews[0].rating;
        this.newReview.isPurchased = this.reviews[0].isPurchased;
        this.newReview.bookIsbn = this.book.isbn;
      }
    }
  }

  //pasa la fecha a un string si existe
  getDate(): any {
    if (this.book.publishDate != null) {
      let date: string;
      date = this.book.publishDate.toString();
      const datePart = date.split('T');
      return datePart[0];
    }
    return 'Sin fecha de publicación';
  }

  toggleReview(): void {
    this.isReviewOpen = !this.isReviewOpen;
  }

  bookTransitions(s: number, n: number) {
    this.bookService
      .getAllBooks(String(s), String(n))
      .subscribe((response: any) => {
        this.book = response.content.map((book: { id: any }) => ({
          ...book,
          viewTransitionName: `view-transition-name: book-${book.id}`,
        }));
      });
  }

  nReviews(): number {
    if (this.book.nReviews === null) {
      return 0;
    } else {
      return this.book.nReviews;
    }
  }

  validateRating() {
    if (this.newReview.rating < 1) {
      this.newReview.rating = 1;
    } else if (this.newReview.rating > 5) {
      this.newReview.rating = 5;
    }
  }

  userModRev(): boolean {
    if (this.userModReview && this.userPostedReview) {
      this.userModReview = false;
    } else if (!this.userModReview && this.userPostedReview) {
      this.userModReview = true;
    }

    if (this.userModReview) {
      return true;
    }
    return false;
  }

  setRate(rate: number): void {
    this.newReview.rating = rate;
  }

  //este método controla el índice de ngFor para las gráficas
  trackByIndex(index: number, item: any): number {
    return index;
  }

  removeReview(): void {
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

    if (this.reviews[0].user.id === this.getUserId()) {
      this.newReview.id = this.reviews[0].id;

      AuthService.isClient().subscribe((isClient: boolean) => {
        if (isClient === true) {
          this.swlAlerts
            .alertConfirmation(
              'Eliminar reseña',
              '¿Estas seguro de eliminar esta reseña?'
            )
            .then((result) => {
              if (result.isConfirmed) {
                this.reviewService.deleteReview(this.isbn).subscribe({
                  next: (response) => {
                    this.swlAlerts.showToastSuccess(
                      'Su reseña ha sido eliminada'
                    );

                    //reinicio variable review
                    this.newReview = new ReviewPost();
                    this.newReview.rating = 0;

                    this.userPostedReview = false; //variable de control, usuario no tiene review
                    this.getReviewsByIsbn(this.isbn); // Actualiza las reseñas después de eliminar
                    this.getBookByIsbn(this.isbn); //obtengo el libro actualizado
                    this.nReviews(); //obtengo el número de reviews
                    this.userModRev(); //cierro el dialogo de modificar review
                  },
                  error: (error) => {
                    this.swlAlerts.showToastError('No se ha podido eliminar');
                  },
                });
              }
            });
        } else {
          this.swlAlerts.alertError(
            'No puedes borrar una reseña que no has publicado'
          );
        }
      });
    }
  }

  removeAdminReview(id: number): void {
    if (sessionStorage.getItem('ACCESS_TOKEN') == null) {
      this.router.navigate(['login']);
      return;
    }

    this.swlAlerts
      .alertConfirmation(
        'Eliminar reseña',
        '¿Estas seguro de eliminar esta reseña?'
      )
      .then((result) => {
        if (result.isConfirmed) {
          this.reviewService.deleteAdminReview(id).subscribe({
            next: (response) => {
              this.swlAlerts.showToastSuccess('Eliminada reseña del usuario ' + response.user.username);
              this.getReviewsByIsbn(this.isbn);
              this.getBookByIsbn(this.isbn);
            },
            error: (error) => {
              this.swlAlerts.showToastError('No se ha podido eliminar');
            },
          });
        } else if (result.isDismissed) {
          return;
        }
      });
  }
}
