import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { authGuard } from './auth.guard';

// Define the routes for the application
const routes: Routes = [
  // Default route, loads PublicModule lazily
  { path: '', loadChildren: () => import('./public/public.module').then((m) => m.PublicModule) },
  
  // Login route, loads LoginComponent when the path is '/login'
  { path: 'login', component: LoginComponent },
  
  // Admin route, loads AdminModule lazily and requires authentication
  { path: 'transact', loadChildren: () => import('./admin/admin.module').then((m) => m.AdminModule), canActivate: [authGuard] },
];

@NgModule({
  // Import the configured routes
  imports: [RouterModule.forRoot(routes)],
  
  // Export the RouterModule to be used in the application
  exports: [RouterModule]
})
export class AppRoutingModule { }
