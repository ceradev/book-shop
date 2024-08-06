import { Pageable } from "./sharedEntities/pageable";

export class BookSearchCriteria {

    isbn: string;
    title: string;
    authorFirstName: string;
    authorLastName: string;
    editorial: string;
    synopsis: string;
    edition: string;
    minPrice: number;
    maxPrice: number;
    publishedDate: Date; // Puedes usar Date en lugar de LocalDateTime en TypeScript
    genres: string[];
    pageable:Pageable;


}
