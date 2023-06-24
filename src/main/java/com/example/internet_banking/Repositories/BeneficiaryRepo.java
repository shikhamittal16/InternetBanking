package com.example.internet_banking.Repositories;

import com.example.internet_banking.Entities.Beneficiaries;
import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BeneficiaryRepo extends CrudRepository<Beneficiaries, Long> {

    @Query("SELECT b FROM Beneficiaries b WHERE b.userAccounts =:userAccount ")
    List<Beneficiaries> fetchAllByUserAccount(UserAccountInfo userAccount);
}
