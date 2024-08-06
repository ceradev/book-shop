import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../../models/Book';
import { FavouritesDto } from '../../models/FavouritesDto';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FavouritesService {
  constructor(private http: HttpClient) {}

  getUserFavs(pageN: string, pageS: string): Observable<Book[]> {
    let queryParams = new HttpParams();
    queryParams = queryParams.set('pageNumber', pageN);
    queryParams = queryParams.set('pageSize', pageS);

    console.log('estos son los query' + queryParams);

    return this.http.get<Book[]>(`${environment.urlApi + '/favourites'}`, {
      params: queryParams,
    });
  }

  addFavourite(bookId: string) {
    return this.http.post<FavouritesDto>(
      `${environment.urlApi + '/favourites'}/${bookId}`,
      { params: null }
    );
  }

  deleteFavourite(bookId: string) {
    return this.http.delete<FavouritesDto>(
      `${environment.urlApi + '/favourites'}/${bookId}`,
    );
  }
}
