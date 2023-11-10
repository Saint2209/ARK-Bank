import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Sample user data for authentication
  users: any[] = [
    {
      id: 1,
      name: 'David',
      username: 'david',
      password: 'abc'
    },
    {
      id: 2,
      name: 'XYZ',
      username: 'XYZ',
      password: 'abc'
    }
  ];

  // Variable to store the current user session
  session: any;
  // initialise account number
  private accNo: number | undefined;

  private baseUrl = 'http://localhost:8080/banking';
  //error var
  private err: string = "";

  constructor(private router: Router, private http: HttpClient) {
    // Retrieve session data from local storage if available
    let session: any = localStorage.getItem('session');
    if (session) {
      // Initialize the session variable
      this.session = session;
    }

    let accNo: any = localStorage.getItem('accountNumber');
    if (accNo) {
      // Initialize the acc variable
      this.accNo = accNo;
    }
  }

  // Function to generate a random session ID
  generateSessionID(length: number = 32): string {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let sessionId = '';

    for (let i = 0; i < length; i++) {
      const randomIndex = Math.floor(Math.random() * characters.length);
      sessionId += characters.charAt(randomIndex);
    }

    return sessionId;
  }

  yourApiBaseUrl = "http://localhost:8080"

  // Function to store the session ID via API
  storeSessionID(accountNumber: number, sessionId: string) {
    // Replace 'your-api-endpoint' with the actual base URL of your Spring Boot app
    return this.http.post<any>(
      `${this.yourApiBaseUrl}/banking/store-session-id`,
      { accountNumber, sessionId }
    ).subscribe(
      // Successful response callback
      res => {
        // Assuming you want to perform any action upon successful storage of the session ID
        console.log('Session ID stored successfully:', res);
      },
      // Error handling callback
      err => {
        console.error('Error storing session ID:', err);
        this.err = `Error storing session ID: ${err}`;
      }
    );
  }


  // Function to authenticate user and retrieve additional user data from API
  authenticateUser(accountNumber: string, password: string): Observable<any> {
    // Log the accountNumber for debugging purposes
    console.log(accountNumber);
  
    // Make a POST request to the authentication endpoint and return the Observable
    return this.http.post<any>(
      `${this.yourApiBaseUrl}/banking/authenticate`,
      { accountNumber, password }
    ).pipe(
      tap(res => {
        // Assuming res contains user data, update the accNo property
        this.accNo = res;
  
        // Log the updated accNo for debugging purposes
        console.log("Auth-Received:", this.accNo);
      }),
      catchError(err => {
        // Log the error for further investigation
        console.error("API Error:", err);
  
        // Display an alert for the user in case of an error
        alert("An error has occurred while contacting the API");
        this.err = `Error retrieving account ID: ${err}`;
  
        // Propagate the error to the calling code
        throw err;
      })
    );
  }





  // Function to handle user login
  async login(accountNumber: string, password: string): Promise<boolean> {
    // Send a POST request to authenticate the user
    console.log("Sending authentication request...");

    // Call the authenticateUser function
    await this.authenticateUser(accountNumber, password).toPromise();

    // Log the current accNo for debugging purposes
    console.log("Current accNo:", this.accNo);

    // Check if authentication was successful
    if (this.accNo && this.err === "") {
      // Generate a random session ID
      const session = this.generateSessionID();

      // Store session ID locally
      localStorage.setItem('session', session);
      console.log(session)
      
      // Make an API call to store the session ID
      console.log("Store Session request...");
      this.storeSessionID(this.accNo, session);

      console.log("Login successful!");
      if (this.err === "") {
        // Return true to indicate successful login
        return true;
      }
      return false
    } else {
      // Log an error message if authentication failed
      console.error("Login failed. Authentication unsuccessful.");

      // Return false to indicate unsuccessful login
      return false;
    }
  }

  // Function to handle user logout
  logout() {
    // Clear the session, remove it from local storage, and navigate to the default route
    this.session = undefined;
    localStorage.removeItem('session');
    this.router.navigateByUrl('/');
  }

  // Function to retrieve the session ID
  getSessionID(): string | null {
    return this.session ? this.session : null;
  }

  // Function to retrieve the session ID
  getAccountNumber(): string | null {
    return this.accNo ? `${this.accNo}` : null;
  }

  getAccountNumberBySession(sessionId: string): Observable<number> {
    const url = `${this.baseUrl}/accountNumber?sessionId=${sessionId}`;
    return this.http.get<number>(url);
  }
}
