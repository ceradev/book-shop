import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Book } from '../../models/Book';
import { BookData } from '../../models/book-data';
import { Genres } from '../../models/Genres';
import { BookSearchCriteria } from '../../models/book-search-criteria';
import { Pageable } from '../../models/sharedEntities/pageable';
import { environment } from '../../../../environments/environment';
import { BookDetails } from '../../models/book-details';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  public bookData: BookData;

  constructor(private http: HttpClient) {}

  getBookById(id: string): Observable<BookDetails> {
    return this.http.get<BookDetails>(`${environment.urlApi + '/books'}/${id}`);
  }

  createBook(book: BookData, image: File) {
    //esto puede que haya que revisarlo con los cambios del back
    const formData = new FormData();
    let jsonData: string = JSON.stringify(book);
    formData.append('book', jsonData);
    formData.append('image', image);
    formData.forEach((value, key) => {
      console.log(key, value);
    });

    return this.http.post<any>(environment.urlApi + '/books', formData);
  }

  deleteBook(id: string) {
    return this.http.delete<Book>(`${environment.urlApi + '/books'}/${id}`);
  }

  editBook(id: string, book: Book): Observable<Book> {
    return this.http.put<Book>(`${environment.urlApi + '/books'}/${id}`, book);
  }

  searchBook(
    q: string,
    status: string,
    pageN: number,
    pageS: number
  ): Observable<Book[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('pageNumber', pageN);
    queryParams = queryParams.set('pageSize', pageS);
    queryParams = queryParams.set('status', status);

    return this.http.get<Book[]>(`${environment.urlApi}/books/search?q=${q}`, {
      params: queryParams,
    });
  }

  changeStatusBook(isbn: string, status: string) {
    return this.http.put<Book>(
      `${environment.urlApi}/books/status/${isbn}/${status}`,
      null
    );
  }

  //shearch filters
  findBookByTitle(title: string, author: string, genre: string) {
    let parameters: string = '';

    if (title != null) {
      parameters = parameters + 'title=' + title + '&';
    } else {
      parameters = parameters + 'title=&';
    }

    if (author != null) {
      parameters = parameters + 'author=' + author;
    } else {
      parameters = parameters + 'author=&';
    }

    if (genre != null) {
      parameters = parameters + 'genre=' + genre;
    } else {
      parameters = parameters + 'genre=';
    }

    return this.http.get<Book>(
      `${environment.urlApi + '/books'}?${parameters}`
    );
  }

  getAllBooks(pageN: string, pageS: string): Observable<Book[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('pageNumber', pageN);
    queryParams = queryParams.set('pageSize', pageS);

    console.log('estos son los query' + queryParams);

    return this.http.get<Book[]>(environment.urlApi + '/books', {
      params: queryParams,
    });
  }

  getAllBooksPending(pageN: string, pageS: string): Observable<Book[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('pageNumber', pageN);
    queryParams = queryParams.set('pageSize', pageS);

    return this.http.get<Book[]>(environment.urlApi + '/books/pending', {
      params: queryParams,
    });
  }

  getBooksBySellerAndStatus(
    status: string,
    pageN: number,
    pageS: number
  ): Observable<Book[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('pageNumber', pageN);
    queryParams = queryParams.set('pageSize', pageS);
    queryParams = queryParams.set('status', status);

    return this.http.get<Book[]>(environment.urlApi + '/books/seller', {
      params: queryParams,
    });
  }

  getAllGenre(): Observable<Genres[]> {
    return this.http
      .get(environment.urlApi + '/genres')
      .pipe(map((response) => response as Genres[]));
  }

  getFilterBooks(page: number, nbooks: number, bookSearch: BookSearchCriteria) {
    const pageable = new Pageable(page, nbooks, null);
    bookSearch.pageable = pageable;
    //esto lo tenia como un array book
    return this.http.post<any>(
      environment.urlApi + '/books/search',
      bookSearch
    );
  }

  searchBooksByQueryString(query: string, nPage: number, nSize: number) {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('pageNumber', nPage);
    queryParams = queryParams.set('pageSize', nSize);
    queryParams = queryParams.set('q', query);
    queryParams = queryParams.set('status', 'PUBLISHED');

    return this.http.get<Book[]>(environment.urlApi + '/books/search', {
      params: queryParams,
    });
  }
}
