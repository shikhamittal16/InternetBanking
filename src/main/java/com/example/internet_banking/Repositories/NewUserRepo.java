package com.example.internet_banking.Repositories;
import com.example.internet_banking.Entities.NewUserDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NewUserRepo extends CrudRepository<NewUserDetails, Long> {

}
