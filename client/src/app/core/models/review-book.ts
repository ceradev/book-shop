import { Author } from "./Author";
import { User } from "./User";

export class ReviewBook {
    id: number;
    user: User;
    rating: number;
    comment: string;
    isPurchased: boolean;
    isbn: string;
    title: string;
    author: Author;
}
