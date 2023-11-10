package com.arkbank.arkbankbe.repository;

import com.arkbank.arkbankbe.entity.TransactionHistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByBankAccount_AccountNumberOrderByTransactionDateDesc(Integer accountNumber);
}
