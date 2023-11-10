package com.arkbank.arkbankbe.repository;

import com.arkbank.arkbankbe.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByAccountNumberAndPassword(Integer accountNumber, String password);
    Optional<BankAccount> findBySession(String session);
}
