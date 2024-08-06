import { Routes } from '@angular/router';
import { TemplateSimpleComponent } from './shared/components/layouts/template-simple/template-simple.component';
import { TemplateDecoratedComponent } from './shared/components/layouts/template-decorated/template-decorated.component';
import { NotFoundComponent } from './pages/global/not-found/not-found.component';
import { authGuard } from './core/guards/auth.guard';
import { AuthService } from './core/services/auth/auth.service';

export const routes: Routes = [
  {
    path: '',
    component: TemplateSimpleComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./pages/global/global.routes').then((m) => m.GLOBAL_ROUTES),
        
      },
      {
        path: '',
        loadChildren: () =>
          import('./pages/client/client.routes').then((m) => m.CLIENT_ROUTES),
        canActivate:[authGuard]
      },
      {
        path: '',
        loadChildren: () =>
          import('./pages/seller/seller.routes').then((m) => m.SELLER_ROUTES),
        canActivate:[authGuard]
      },
      {
        path: '',
        loadChildren: () =>
          import('./pages/admin/admin.routes').then((m) => m.ADMIN_ROUTES),
        canActivate:[authGuard]
      },
    ],
  },
  {
    path: '',
    component: TemplateDecoratedComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./pages/auth/auth.routes').then((m) => m.AUTH_ROUTES),
      },
    ],
  },
  { path: '**', component: NotFoundComponent} //esta ruta TIENE que ser la ultima
];
