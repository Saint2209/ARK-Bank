package com.arkbank.arkbankbe.service;

import com.arkbank.arkbankbe.entity.BankAccount;
import com.arkbank.arkbankbe.entity.TransactionHistory;
import com.arkbank.arkbankbe.exception.AccountNotFoundException;
import com.arkbank.arkbankbe.exception.InsufficientBalanceException;
import com.arkbank.arkbankbe.repository.BankAccountRepository;
import com.arkbank.arkbankbe.repository.TransactionHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankService {

    //Auto pulls repo
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    public BankAccount getAccountBalance(Integer accountNumber) {
        BankAccount bankAccount = bankAccountRepository.findById(accountNumber).orElse(null);
        if (bankAccount == null) {
            throw new AccountNotFoundException(String.format("Account number (%s) not found", accountNumber));
        }
        return bankAccount;
    }

    @Transactional
    public void debitBalance(Integer accountNumber, Long amountToBeDebited) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(accountNumber);

        if (optionalAccount.isPresent()) {
            BankAccount bankAccount = optionalAccount.get();

            Long currentBalance = bankAccount.getBalance();

            if (currentBalance.compareTo(amountToBeDebited) >= 0) {
                // Sufficient balance for the debit
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.setDebit(amountToBeDebited);
                transactionHistory.setBankAccount(bankAccount);
                transactionHistory.setCredit(0L);
                // Set the transactionDate
                transactionHistory.setTransactionDate(new Timestamp(System.currentTimeMillis()));

                Long newBalance = currentBalance - amountToBeDebited;
                bankAccount.setBalance(newBalance);

                bankAccountRepository.save(bankAccount);
                transactionHistoryRepository.save(transactionHistory);
            } else {
                // Handle insufficient balance
                throw new InsufficientBalanceException(String.format("Insufficient balance: %s", currentBalance));
            }
        } else {
            // Handle account not found
            throw new AccountNotFoundException(String.format("Account number (%s) not found", accountNumber));
        }
    }

    @Transactional
    public void creditBalance(Integer accountNumber, Long amountToBeCredited) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(accountNumber);

        if (optionalAccount.isPresent()) {
            BankAccount bankAccount = optionalAccount.get();

            Long currentBalance = bankAccount.getBalance();

            // Calculate the new balance after crediting
            Long newBalance = currentBalance + amountToBeCredited;

            // Update the bank account's balance
            bankAccount.setBalance(newBalance);

            // Save the updated bank account
            bankAccountRepository.save(bankAccount);

            // Create a transaction history record for the credit
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setCredit(amountToBeCredited);
            transactionHistory.setBankAccount(bankAccount);
            transactionHistory.setDebit(0L);
            // Set the transactionDate
            transactionHistory.setTransactionDate(new Timestamp(System.currentTimeMillis()));

            transactionHistoryRepository.save(transactionHistory);
        } else {
            // Handle account not found
            throw new AccountNotFoundException(String.format("Account number (%s) not found", accountNumber));
        }
    }

    public void scheduleRecurringPayment(Integer accountNumber, Integer accountNumber2, Long amountToBeDebited) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(accountNumber);
        Optional<BankAccount> optionalAccount2 = bankAccountRepository.findById(accountNumber2);

        if (optionalAccount.isPresent() && optionalAccount2.isPresent()) {
            BankAccount bankAccount = optionalAccount.get();
            LocalDate currentDate = LocalDate.now();
            LocalDate nextPaymentDate = bankAccount.getNextPaymentDate().toLocalDate();

            if (nextPaymentDate.isBefore(currentDate)) {
                // Payment is due, process it
                debitBalance(accountNumber, amountToBeDebited);
                creditBalance(accountNumber2, amountToBeDebited);

                // Update the next payment date (e.g., set it for the next month)
                LocalDate newNextPaymentDate = currentDate.plusMonths(1);
                bankAccount.setNextPaymentDate(Date.valueOf(newNextPaymentDate));

                bankAccountRepository.save(bankAccount);
            }
        } else {
            // Handle account not found
            throw new AccountNotFoundException(String.format("Account number (%s) not found", accountNumber));
        }
    }

    public void transferFunds(Integer senderAccountNumber, Integer receiverAccountNumber, Long amount) {
        debitBalance(senderAccountNumber, amount);

        creditBalance(receiverAccountNumber, amount);
    }

    
    public void authenticateUser(Integer accountNumber, String password) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findByAccountNumberAndPassword(accountNumber,
                password);

        if (optionalAccount.isEmpty()) {
            // Authentication failed, handle accordingly (throw exception, log, etc.)
            throw new RuntimeException("Authentication failed");
        }
        // Authentication successful
    }

    @Transactional
    public BankAccount storeSessionId(Integer accountNumber, String sessionId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountNumber)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        // Set the session ID in the bank account entity
        bankAccount.setSession(sessionId);

        // Save the updated bank account entity
        return bankAccountRepository.save(bankAccount);
    }

    /**
     * Retrieves a bank account based on the provided account number.
     *
     * @param accountNumber The account number to search for.
     * @return The bank account if found.
     * @throws AccountNotFoundException If the account is not found.
     */
    public BankAccount getAccountByAccountNumber(Integer accountNumber) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(accountNumber);

        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new AccountNotFoundException(String.format("Account number (%s) not found", accountNumber));
        }
    }

    public List<TransactionHistory> getTransactionHistory(Integer accountNumber) {
        BankAccount bankAccount = getAccountByAccountNumber(accountNumber);
        List<TransactionHistory> transactionHistory = transactionHistoryRepository
                .findByBankAccount_AccountNumberOrderByTransactionDateDesc(accountNumber);

        // Populate account number, debit, and credit in the response
        return transactionHistory.stream()
                .peek(th -> {
                    th.setBankAccount(bankAccount);
                    th.setDebit(th.getDebit());
                    th.setCredit(th.getCredit());
                    th.setTransactionDate(th.getTransactionDate());
                })
                .collect(Collectors.toList());
    }

    public Integer getAccountNumberBySessionID(String sessionId) {
        Optional<BankAccount> optionalAccount = bankAccountRepository.findBySession(sessionId);
        return optionalAccount.map(BankAccount::getAccountNumber)
                .orElse(null);
    }
}
