import { Component } from '@angular/core';

@Component({
  // Selector for the component in HTML files
  selector: 'app-root',
  
  // HTML template file for the component
  templateUrl: './app.component.html',
  
  // Stylesheet file for the component
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  // Property to store the title of the app
  title = 'ARK_Bank';
}
