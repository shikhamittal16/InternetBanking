package com.example.internet_banking.Repositories;

import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface UserAccountRepo extends CrudRepository<UserAccountInfo,Long> {

    @Query("SELECT u FROM UserAccountInfo u WHERE u.accountNo = :accountNo")
    UserAccountInfo findByAccountNumber(String accountNo);

    @Query("SELECT u.transactionLimit FROM UserAccountInfo u WHERE u.accountNo =:accountNo")
    String findTransactionLimitOfUser(String accountNo);

    @Query("SELECT u.depositBalance FROM UserAccountInfo u WHERE u.accountNo =:accountNo")
    BigDecimal findDepositAmountOfUser(String accountNo);
}

