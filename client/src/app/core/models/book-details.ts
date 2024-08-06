import { Author } from "./Author";
import { Genres } from "./Genres";

export class BookDetails {
    
    isbn: string;
    cover: string;
    title: string;
    author: Author;
    editorial: string;
    synopsis: string | null;
    edition: string;
    status : string;
    stock: number;
    price: number;
    publishDate: Date | null;
    genres: Genres[];
    salesAmount: number | null;
    nReviews:number;
    reviewMean:number;
    //static reviews: any;

    constructor(){
        this.genres=[];
        
    }
        
    isFavourite: boolean = false;

}
