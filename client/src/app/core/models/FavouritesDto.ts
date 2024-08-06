export class FavouritesDto {
    private  userId:string;
    bookIsbn:string;

    public getUserId(): string {
        return this.userId;
    }

    public setUserId(userId: string): void {
        this.userId = userId;
    }
    
    public getBookIsbn(): string {
        return this.bookIsbn;
    }

    public setBookIsbn(bookIsbn: string): void {
        this.bookIsbn = bookIsbn;
    }
}