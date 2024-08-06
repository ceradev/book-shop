export class Roles {
    private book_id:string;
    private user_id:number;

    public getBook_id(): string {
        return this.book_id;
    }

    public setBook_id(book_id: string): void {
        this.book_id = book_id;
    }

    public getUser_id(): number {
        return this.user_id;
    }

    public setUser_id(user_id: number): void {
        this.user_id = user_id;
    }
}
