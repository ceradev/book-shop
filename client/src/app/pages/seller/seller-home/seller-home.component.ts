import { Component, OnInit } from '@angular/core';
import { Book } from '../../../core/models/Book';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BookService } from '@services/book/book-service';
import { Pagination } from '@models/sharedEntities/pagination';

@Component({
  selector: 'app-seller-home',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './seller-home.component.html',
  styleUrl: './seller-home.component.css',
})
export class SellerHomeComponent implements OnInit {
  publishedBooks: Book[] = [];
  pendingBooks: Book[] = [];
  rejectedBooks: Book[] = [];
  paginationPublished: Pagination = new Pagination();
  paginationPending: Pagination = new Pagination();
  paginationRejected: Pagination = new Pagination();
  pagesPublished: number[] = [];
  pagesPending: number[] = [];
  pagesRejected: number[] = [];

  constructor(private bookService: BookService) {}
 

  ngOnInit(): void {
    this.paginationPublished.pageNumber = 0;
    this.paginationPending.pageNumber = 0;
    this.paginationRejected.pageNumber = 0;
    this.paginationPublished.pageSize = 3;
    this.paginationPending.pageSize = 3;
    this.paginationRejected.pageSize = 3;

    this.getPublishedBooks(
      this.paginationPublished.pageNumber,
      this.paginationPublished.pageSize
    );
    this.getPendingBooks(
      this.paginationPending.pageNumber,
      this.paginationPending.pageSize
    );
    this.getRejectedBooks(
      this.paginationRejected.pageNumber,
      this.paginationRejected.pageSize
    );
  }

  getPublishedBooks(pageNumber: number, pageSize: number) {
    this.bookService
      .getBooksBySellerAndStatus(
        'PUBLISHED',
        this.paginationPublished.pageNumber,
        this.paginationPublished.pageSize
      )
      .subscribe(
        (response: any) => {
          this.publishedBooks = response.content;
          this.paginationPublished.totalElements = response.totalElements;
          this.paginationPublished.totalPages = response.totalPages;
          this.paginationPublished.first = response.first;
          this.paginationPublished.last = response.last;
          this.pagesPublished = [];
          for (let i = 0; i < this.paginationPublished.totalPages; i++) {
            this.pagesPublished[i] = i + 1;
          }
        },
        (error) => {
          console.log(error);
        }
      );
  }

  getPendingBooks(pageNumber: number, pageSize: number) {
    this.bookService
      .getBooksBySellerAndStatus(
        'PENDING',
        this.paginationPending.pageNumber,
        this.paginationPending.pageSize
      )
      .subscribe(
        (response: any) => {
          this.pendingBooks = response.content;
          this.paginationPending.totalElements = response.totalElements;
          this.paginationPending.totalPages = response.totalPages;
          this.paginationPending.first = response.first;
          this.paginationPending.last = response.last;
          this.pagesPending = [];
          for (let i = 0; i < this.paginationPending.totalPages; i++) {
            this.pagesPending[i] = i + 1;
          }
        },
        (error) => {
          console.log(error);
        }
      );
  }

  getRejectedBooks(pageNumber: number, pageSize: number) {
    this.bookService
      .getBooksBySellerAndStatus(
        'REJECTED',
        this.paginationRejected.pageNumber,
        this.paginationRejected.pageSize
      )
      .subscribe(
        (response: any) => {
          this.rejectedBooks = response.content;
          this.paginationRejected.totalElements = response.totalElements;
          this.paginationRejected.totalPages = response.totalPages;
          this.paginationRejected.first = response.first;
          this.paginationRejected.last = response.last;
          this.pagesRejected = [];
          for (let i = 0; i < this.paginationRejected.totalPages; i++) {
            this.pagesRejected[i] = i + 1;
          }
        },
        (error) => {
          console.log(error);
        }
      );
  }

  changePagePublished(pageNumber: number) {
    if (pageNumber >= this.paginationPublished.totalPages) {
      pageNumber = this.paginationPublished.totalPages - 1;
    } else if (pageNumber < 0) {
      pageNumber = 0;
    }
    this.paginationPublished.pageNumber = pageNumber;
    this.getPublishedBooks(pageNumber, this.paginationPublished.pageSize);
  }

  changePagePending(pageNumber: number) {
    if (pageNumber >= this.paginationPending.totalPages) {
      pageNumber = this.paginationPending.totalPages - 1;
    } else if (pageNumber < 0) {
      pageNumber = 0;
    }
    this.paginationPending.pageNumber = pageNumber;
    this.getPendingBooks(pageNumber, this.paginationPending.pageSize);
  }

  changePageRejected(pageNumber: number) {
    if (pageNumber >= this.paginationRejected.totalPages) {
      pageNumber = this.paginationRejected.totalPages - 1;
    } else if (pageNumber < 0) {
      pageNumber = 0;
    }
    this.paginationRejected.pageNumber = pageNumber;
    this.getRejectedBooks(pageNumber, this.paginationRejected.pageSize);
  }


  
}
