package com.example.internet_banking.Repositories;

import com.example.internet_banking.Entities.Transactions;
import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionsRepo extends CrudRepository<Transactions,Long> {

    @Query("SELECT t FROM Transactions t WHERE t.userTransactions = :userAccount")
    List<Transactions> findAllTransactionsByUserAccount(@Param("userAccount") UserAccountInfo userAccount);
}
