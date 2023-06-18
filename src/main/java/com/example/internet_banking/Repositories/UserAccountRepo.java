package com.example.internet_banking.Repositories;

import com.example.internet_banking.Entities.UserAccountInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepo extends CrudRepository<UserAccountInfo,String> {
}
