import { Book } from "./Book";
import { User } from "./User";

export interface Sales {
     id:number;
     client:User;
     book:Book
     saleDate:Date;
     quantity:number;
}
