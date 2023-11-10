import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { inject } from '@angular/core';

// Define the authorization guard function
export const authGuard: CanActivateFn = (route, state) => {
  // Check if the user is authenticated using the AuthService
  if (inject(AuthService).session) {
    // If authenticated, allow access
    return true;
  } else {
    // If not authenticated, redirect to the home page using the Router
    inject(Router).navigateByUrl('/');
    // Return false to indicate that access is not allowed
    return false;
  }
};