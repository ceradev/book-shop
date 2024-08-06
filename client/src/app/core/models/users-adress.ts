export class UsersAdress {
    private id:number;
    private address:number;
    private user_id:number;

    public getId(): number {
        return this.id;
    }

    public setId(id: number): void {
        this.id = id;
    }

    public getAddress(): number {
        return this.address;
    }

    public setAddress(address: number): void {
        this.address = address;
    }

    public getUser_id(): number {
        return this.user_id;
    }

    public setUser_id(user_id: number): void {
        this.user_id = user_id;
    }
}
