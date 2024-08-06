import { Routes } from "@angular/router";
import { ClientManagementComponent } from "./client-management/client-management.component";
import { AdminViewClientComponent } from "./admin-view-client/admin-view-client.component";
import { ClientHomeComponent } from "@pages/client/client-home/client-home.component";


export const ADMIN_ROUTES: Routes = [
    {path: 'clients', component:ClientManagementComponent},
    {path: 'home', component:ClientHomeComponent},
    {path: 'clients/:id', component:AdminViewClientComponent}
]