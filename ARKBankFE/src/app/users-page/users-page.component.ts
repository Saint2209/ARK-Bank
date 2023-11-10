// users-page.component.ts
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-users-page',
  templateUrl: './users-page.component.html',
  styleUrls: ['./users-page.component.css']
})
export class UsersPageComponent implements OnInit {
  balance: number = 0;
  transactionHistory: any[] = [];
  transactForm: FormGroup;
  debitOrderForm: FormGroup;
  yourApiBaseUrl: any = "http://localhost:8080/banking";
  accountNumber: number | null = null;

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private formBuilder: FormBuilder
  ) {
    // Initialize forms in the constructor
    this.debitOrderForm = this.formBuilder.group({
      accountNumber: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]],
      amountToBeDebited: ['', [Validators.required, Validators.min(50)]],
    });

    this.transactForm = this.formBuilder.group({
      receiverAccountNumber: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]],
      amount: ['', [Validators.required, Validators.min(50)]],
    });
  }

  ngOnInit() {
    // Fetch user data and transaction history on component initialization
    this.initUserData();
    console.log("ngonint")
  }

  async initUserData() {
    // Get session and account information
    const sessionId = this.authService.getSessionID();
    if (sessionId !== null) {
    try {
      // Fetch account number using session ID
      this.accountNumber = (await this.authService.getAccountNumberBySession(sessionId).toPromise()) || 0; // Replace 0 with your default value
      console.log('Fetched Account Number:', this.accountNumber);
  
      // Do something with the fetched account number
      if (sessionId && this.accountNumber !== null) {
        console.log("Entered", this.accountNumber);
        // Fetch user data and transaction history
        await this.fetchUserData(`${this.accountNumber}`);
        await this.fetchTransactionHistory(`${this.accountNumber}`);
      }
    } catch (error) {
      console.error('Error fetching account number:', error);
      this.logout();
      // Handle the error
    }
    }
    console.log("sessionId", sessionId);
  }
  
  async fetchUserData(accountNumber: string) {
    // Fetch user balance
    try {
      const response = await this.http.get<any>(`${this.yourApiBaseUrl}/accountBalance?accountNumber=${accountNumber}`).toPromise();
      this.balance = response;
      console.log('User Balance:', this.balance);
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  }

  async fetchTransactionHistory(accountNumber: string) {
    // Fetch transaction history
    try {
      const historyData = await this.http.get<any[]>(`${this.yourApiBaseUrl}/transactionHistory?accountNumber=${accountNumber}`).toPromise();
      this.transactionHistory = historyData || [];
      console.log('Transaction History:', this.transactionHistory);
    } catch (error) {
      console.error('Error fetching transaction history:', error);
    }
  }

  logout() {
    // Logout user
    this.authService.logout();
  }

  transact() {
    // Perform transaction
    if (this.transactForm.valid) {
      // Construct transaction data
      const senderAccountNumber = this.accountNumber;
      const receiverAccountNumber = this.transactForm.value.receiverAccountNumber;
      const amount = this.transactForm.value.amount;

      // Make HTTP request to initiate a transaction
      this.http.post<any>(
        `${this.yourApiBaseUrl}/transfer/funds`,
        { senderAccountNumber, receiverAccountNumber, amount }
      ).subscribe(
        (response) => {
          // Handle successful transaction
          console.log('Transaction successful!', response);
          alert(`Transaction successful!`);
          this.transactForm.reset({});
          // Reload the page after a successful transaction
          location.reload();
        },
        (error) => {
          console.error('Error processing transaction:', error);
        }
      );
    } else {
      console.log('Form is invalid. Please check your inputs.');
    }
  }

  // Debit order
  debitOrder() {
    // Start recurring debit order
    if (this.debitOrderForm.valid) {
      const senderAccountNumber = this.accountNumber;
      const receiverAccountNumber = this.debitOrderForm.value.accountNumber;
      const amountToBeDebited = this.debitOrderForm.value.amountToBeDebited;

      // Make HTTP request to start recurring debit order
      this.http.post(`${this.yourApiBaseUrl}/start-recurring`, { senderAccountNumber, receiverAccountNumber, amountToBeDebited }).subscribe(
        (response) => {
          // Handle successful debit order
          console.log('Debit Order successful!', response);
          alert(`Debit Order successful!`);
          this.debitOrderForm.reset({});
          // Reload the page after a successful transaction
          location.reload();
        },
        (error) => {
          console.error('Error processing debit order:', error);
        }
      );
    } else {
      console.log('Form is invalid. Please check your inputs.');
    }
  }
}
