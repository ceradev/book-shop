import { Author } from "./Author";
import { Genres } from "./Genres";

export interface Book {
    isbn:string;
    cover:string;
    title:string;
    author:Author;
    editorial:string;
    genres:Genres[];
    synopsis:string;
    edition:string;
    status:string;
    price:number;
    reviewMean:number;
    nReviews:number;
    publishDate:string;
    stock : number;
    isLoading: boolean;
    isFavourite:boolean;
}
