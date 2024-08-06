export class Favourites {
    bookIsbn:string;
    isFav:boolean;
    
    public getBookIsbn(): string {
        return this.bookIsbn;
    }

    public setBookIsbn(bookIsbn: string): void {
        this.bookIsbn = bookIsbn;
    }

    public getIsFav(): boolean {
        return this.isFav;
    }

    public setIsFav(isFav: boolean): void {
        this.isFav = isFav;
    }
}
