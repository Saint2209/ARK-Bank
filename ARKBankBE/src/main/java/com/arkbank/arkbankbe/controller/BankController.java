package com.arkbank.arkbankbe.controller;

import com.arkbank.arkbankbe.entity.BankAccount;
import com.arkbank.arkbankbe.entity.TransactionHistory;
import com.arkbank.arkbankbe.exception.AccountNotFoundException;
import com.arkbank.arkbankbe.service.BankService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:4200/")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/start-recurring")
    public ResponseEntity<Void> initiateRecurringPayments(@RequestBody Map<String, Object> request) {
        try {
            // Extract individual parameters from the request
            Integer senderAccountNumber = Integer.parseInt(request.get("senderAccountNumber").toString());
            Integer receiverAccountNumber = Integer.parseInt(request.get("receiverAccountNumber").toString());
            Long amountToBeDebited = Long.parseLong(request.get("amountToBeDebited").toString());

            // Validate the extracted parameters if needed

            // Perform the recurring payment using the extracted parameters
            bankService.scheduleRecurringPayment(senderAccountNumber, receiverAccountNumber, amountToBeDebited);

            // Return a successful response
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException | NullPointerException e) {
            // Handle cases where the string is not a valid integer or parameters are
            // missing
            System.err.println("Invalid input format or missing parameters.");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/accountBalance")
    public ResponseEntity<Long> getAccountBalance(@RequestParam Integer accountNumber) {
        System.out.println("Balance");
        BankAccount bankAccount = bankService.getAccountBalance(accountNumber);

        return ResponseEntity.ok(bankAccount.getBalance());
    }

    @PostMapping("/transfer/funds")
    public ResponseEntity<Void> transferFunds(@RequestBody Map<String, Object> request) {
        System.out.println("\n\n\nTransfer Funds\n\n\n");
        try {
            // Extract individual parameters from the request
            Integer senderAccountNumber = Integer.parseInt(request.get("senderAccountNumber").toString());
            Integer receiverAccountNumber = Integer.parseInt(request.get("receiverAccountNumber").toString());
            Long amount = Long.parseLong(request.get("amount").toString());

            // Perform funds transfer using the extracted parameters
            bankService.transferFunds(senderAccountNumber, receiverAccountNumber, amount);

            // Return a successful response
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException | NullPointerException e) {
            // Handle cases where the string is not a valid integer or parameters are
            // missing
            System.err.println("Invalid input format or missing parameters.");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/transactionHistory")
    public ResponseEntity<List<TransactionHistory>> getTransactionHistory(@RequestParam Integer accountNumber) {
        try {
            List<TransactionHistory> transactionHistory = bankService.getTransactionHistory(accountNumber);
            return ResponseEntity.ok(transactionHistory);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Endpoint to authenticate a user
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody Map<String, String> request) {
        System.out.println("Authenticate");
        try {
            // Convert the string to a long
            Integer accountNumberLong = Integer.parseInt(request.get("accountNumber"));

            // Get the bank account for the provided account number
            BankAccount account = bankService.getAccountByAccountNumber(accountNumberLong);

            // Check if the account exists and perform authentication using the authenticate
            // method
            if (account != null && account.authenticate(accountNumberLong, request.get("password"))) {
                return ResponseEntity.ok(Long.toString(accountNumberLong));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication failed ->" + accountNumberLong);
            }
        } catch (NumberFormatException e) {
            // Handle the case where the string is not a valid integer
            System.err.println("Invalid integer format: " + request.get("accountNumber"));
        }
        return null;
    }

    // Endpoint to store the session ID
    @PostMapping("/store-session-id")
    public ResponseEntity<Object> storeSessionId(@RequestBody Map<String, String> request) {
        System.out.println("Session");
        try {
            Integer accountNumber = Integer.parseInt(request.get("accountNumber"));
            String sessionId = request.get("sessionId");

            BankAccount updatedAccount = bankService.storeSessionId(accountNumber, sessionId);
            return ResponseEntity.ok(updatedAccount);
        } catch (NumberFormatException e) {
            // Handle the case where the accountNumber is not a valid integer
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid accountNumber format");
        } catch (Exception e) {
            // Handle other potential exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request");
        }
    }

    @GetMapping("/accountNumber")
    public ResponseEntity<Integer> getAccountNumberBySession(@RequestParam String sessionId) {
        try {
            Integer accountNumber = bankService.getAccountNumberBySessionID(sessionId);
            if (accountNumber != null) {
                return ResponseEntity.ok(accountNumber);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
