import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  constructor(private router: Router) { }
  ngOnInit() {
      // Check if the session key exists on component initialization
      const sessionExists = localStorage.getItem('session');
  
      if (sessionExists) {
        // Session key exists, redirect to 'admin'
        console.log("landing sess exists")
        this.router.navigateByUrl('/transact');
      }
    }
}
