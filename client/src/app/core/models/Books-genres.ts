export class BooksGenres {
    private book_id:string;
    private genres_id:number;

    public getBook_id(): string {
        return this.book_id;
    }

    public setBook_id(book_id: string): void {
        this.book_id = book_id;
    }

    public getGenres_id(): number {
        return this.genres_id;
    }

    public setGenres_id(genres_id: number): void {
        this.genres_id = genres_id;
    }
}
