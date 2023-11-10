import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form: FormGroup = this.fb.group({
    accountNumber: ['', [Validators.required, Validators.pattern(/^\d{6}$/), Validators.min(0)]],
    password: ['', Validators.required],
  })

  accountNumberValid: boolean = true;

  constructor(private authService: AuthService, 
    private fb: FormBuilder,
    private router: Router) { }

    ngOnInit() {
      // Check if the session key exists on component initialization
      const sessionExists = this.authService.getSessionID();
  
      if (sessionExists) {
        // Session key exists, redirect to 'admin'
        
        console.log(sessionExists);
        this.router.navigateByUrl('/transact');
      }
    }

    checkAccountNumberValidity() {
      // Check if the account number control is valid
      this.accountNumberValid = this.form.get('accountNumber')?.valid || false;
    }
  
    async login() {
      try {
        let loggedIn = await this.authService.login(
          this.form.value.accountNumber,
          this.form.value.password
        );
        console.log("Logged in:", loggedIn)
        if (!loggedIn) {
          console.log("failed")
          alert("Invalid username or password");
        } else {
          location.reload();
        }
      } catch (error) {
        console.error("Error during login:", error);
        alert("An error occurred during login");
      }
    }
}
