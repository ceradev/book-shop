import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReviewsByIsbn } from '../../models/Reviews';
import { Observable } from 'rxjs';
import { ReviewPost } from '../../models/review-post';
import { RequestUserIdBookIsbn } from '@models/request-user-id-book-isbn';
import { ReviewBook } from '@models/review-book';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private urlEndPointAll:string="http://localhost:8089/api/v1/reviews"
  private urlEndPointAllSeller:string="http://localhost:8089/api/v1/reviews/seller"
  private urlEndPointAllAdmin:string="http://localhost:8089/api/v1/reviews/admin"

  constructor(private http:HttpClient) { }


  getAllReviewByIsbn(isbn:string):Observable<ReviewsByIsbn[]>{
    return this.http.get<ReviewsByIsbn[]>(`${this.urlEndPointAll}/${isbn}`);
  }


  //obtiene todas las reviews que ha hecho un usuario
  getAllUserReview():Observable<ReviewBook[]>{
    return this.http.get<ReviewBook[]>(this.urlEndPointAll);
  }

  getSellerBookReview():Observable<ReviewBook[]>{
    return this.http.get<ReviewBook[]>(this.urlEndPointAllSeller);
  }


  postReview(review:ReviewPost){

    return this.http.post<ReviewPost>(this.urlEndPointAll, review);
    
  }

  putReview(review:ReviewPost){

    return this.http.put<ReviewPost>(this.urlEndPointAll, review);

  }

  /*
  deleteReview(review:RequestUserIdBookIsbn){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      body: review
    };
    return this.http.delete<RequestUserIdBookIsbn>(this.urlEndPointAll, httpOptions);
  }*/

  deleteReview(isbn:string){
    return this.http.delete<any>(`${this.urlEndPointAll}/${isbn}`);
  }

  deleteAdminReview(id:number){
    return this.http.delete<any>(`${this.urlEndPointAllAdmin}/${id}`);
  }


  



}
