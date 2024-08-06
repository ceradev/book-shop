import { Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { ResultSearchComponent } from "./result-search/result-search.component";
import { BookDetailsComponent } from "./book-details/book-details.component";
import { UserProfileComponent } from "./user-profile/user-profile.component";
import { authGuard } from "../../core/guards/auth.guard";


export const GLOBAL_ROUTES: Routes = [
    {path: '', component:HomeComponent},
    {path: 'search', component:ResultSearchComponent},
    {path: 'book/details/:id', component:BookDetailsComponent},
    {path: 'profile', component:UserProfileComponent, canActivate:[authGuard]}
]