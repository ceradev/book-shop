import { Routes } from '@angular/router';
import { AddBookComponent } from './add-book/add-book.component';
import { ClientHomeComponent } from '@pages/client/client-home/client-home.component';

export const SELLER_ROUTES: Routes = [
  { path: 'add/book', component: AddBookComponent },
  { path: 'home', component: ClientHomeComponent },
];
