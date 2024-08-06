import { Routes } from "@angular/router";
import { ConfirmPurchaseComponent } from "./confirm-purchase/confirm-purchase.component";
import { CartComponent } from "./cart/cart.component";
import { FavouritesComponent } from "./favourites/favourites.component";


export const CLIENT_ROUTES: Routes = [
    {path : 'checkout', component:ConfirmPurchaseComponent},
    {path : 'cart', component:CartComponent},
    {path : 'favorites', component:FavouritesComponent}
]