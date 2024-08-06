import { CartBook } from "./Cart-book";

export class Cart {
  id: number;
  userId: string;
  totalItems: number;
  amount: number;
  items: CartBook[];

  //variables solo de vista
  static shipping: number = 4.99;
  total : number;


}
