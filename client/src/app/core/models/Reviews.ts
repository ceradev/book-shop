export class ReviewsByIsbn {
    id: number;
    user: {
      id: string;
      username: string;
      name: string;
      surname: string;
      email: string;
    };
    rating: number;
    comment: string;
    isPurchased: boolean;
}
