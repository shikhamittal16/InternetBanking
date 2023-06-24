package com.example.internet_banking.Repositories;

import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepo extends CrudRepository<UserAccountInfo,Long> {

    @Query("SELECT u FROM UserAccountInfo u WHERE u.accountNo = :accountNo")
    UserAccountInfo findByAccountNumber(String accountNo);
}
