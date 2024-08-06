import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Book } from '../../../core/models/Book';
import { Pagination } from '../../../core/models/sharedEntities/pagination';
import { BookService } from '../../../core/services/book/book-service';
import { FormsModule } from '@angular/forms';
import { SwlAlerts } from '../../../shared/utils/swl';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './admin-home.component.html',
  styleUrl: './admin-home.component.css',
})
export class AdminHomeComponent implements OnInit {
  books: Book[] = [];
  filterText: string = '';
  search: string = '';

  pagination: Pagination = new Pagination();
  pages: number[];

  constructor(private bookService: BookService, private swlAlerts: SwlAlerts) {}

  ngOnInit(): void {
    this.pagination.pageNumber = 0;
    this.pagination.pageSize = 5;
    this.filterText = 'PUBLISHED';
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
  }

  getBooks(s: number, n: number) {
    if (this.filterText == 'PENDING') {
      this.bookService.getAllBooksPending(String(s), String(n)).subscribe(
        (response: any) => {
          this.books = response.content;
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
          window.location.reload();
        }
      );
    } else {
      this.bookService.getAllBooks(String(s), String(n)).subscribe(
        (response: any) => {
          this.books = response.content;
          this.pagination.totalElements = response.totalElements;
          this.pagination.totalPages = response.totalPages;
          this.pagination.first = response.first;
          this.pagination.last = response.last;
          this.pages = [];
          console.log(this.books);
          console.log(this.pagination);

          for (let i = 0; i < this.pagination.totalPages; i++) {
            this.pages[i] = i + 1;
          }
        },
        (error) => {
          console.log(error);
          window.location.reload();
        }
      );
    }
  }

  onInputChange(event: any) {
    this.search = event.target.value;
    this.pagination.pageNumber = 0;
    this.searchBooks();
  }

  searchBooks() {
    if (this.search == '') {
      this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
      return;
    }

    this.bookService
      .searchBook(
        this.search,
        this.filterText,
        this.pagination.pageNumber,
        this.pagination.pageSize
      )
      .subscribe(
        (response: any) => {
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
        },
        (error) => {
          console.log(error);
        }
      );
  }

  changeFilter($event: any) {
    this.filterText = $event.target.value;
    this.pagination.pageNumber = 0;
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
  }

  onSelectBooks(event: any) {
    const pageSize = event.target.value;
    this.pagination.pageSize = pageSize;
    this.pagination.pageNumber = 0;
    this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
  }

  changePage(nPage: number) {
    if (nPage >= this.pagination.totalPages) {
      nPage = this.pagination.totalPages - 1;
    } else if (nPage < 0) {
      nPage = 0;
    }
    this.pagination.pageNumber = nPage;
    if (this.search == '') {
      this.getBooks(this.pagination.pageNumber, this.pagination.pageSize);
    } else {
      console.log('nPage: ' + nPage);
      this.searchBooks();
    }
  }

  changeStatusBook(isbn: string, status: string) {
    if (status == 'PUBLISHED') {
      this.swlAlerts
        .alertConfirmation(
          'Publicar libro',
          '¿Estás seguro que quieres publicar este libro?'
        )
        .then((result) => {
          if (result.isConfirmed) {
            this.bookService.changeStatusBook(isbn, status).subscribe(
              (response) => {
                console.log(response);
                this.swlAlerts.showToastSuccess('Libro publicado correctamente');
                this.getBooks(
                  this.pagination.pageNumber,
                  this.pagination.pageSize
                );
              },
              (error) => {
                this.swlAlerts.showToastError('Error al publicar el libro');
                console.log(error);
              }
            );
          }
        });
    } else {
      this.swlAlerts
        .alertConfirmation(
          'Rechazar libro',
          '¿Estás seguro que quieres rechazar este libro?'
        )
        .then((result) => {
          if (result.isConfirmed) {
            this.bookService.changeStatusBook(isbn, status).subscribe(
              (response) => {
                console.log(response);
                this.swlAlerts.showToastSuccess('Libro despublicado correctamente');
                this.getBooks(
                  this.pagination.pageNumber,
                  this.pagination.pageSize
                );
              },
              (error) => {
                this.swlAlerts.showToastError('Error al despublicar el libro');
                console.log(error);
              }
            );
          }
        });
    }
  }

  deleteBook(isbn: string) {
    this.swlAlerts
      .alertConfirmation(
        'Eliminar libro',
        '¿Estás seguro que quieres borrar este libro?'
      )
      .then((result) => {
        if (result.isConfirmed) {
          this.bookService.deleteBook(isbn).subscribe(
            (response) => {
              console.log(response);
              this.swlAlerts.showToastSuccess(
                'Libro eliminado de la base de datos correctamente'
              );
              this.getBooks(
                this.pagination.pageNumber,
                this.pagination.pageSize
              );
            },
            (error) => {
              this.swlAlerts.showToastError(
                'Ha habido un problema al eliminar el libro'
              );
              console.log(error);
            }
          );
        }
      });
  }
}
