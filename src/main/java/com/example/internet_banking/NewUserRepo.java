package com.example.internet_banking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NewUserRepo extends CrudRepository<NewUserDetails , Long> {

    @Query("select u from NewUserDetails u where u.newUserDetailsPK.AccountNo = (select max(u.newUserDetailsPK.AccountNo) from NewUserDetails u)")
    NewUserDetails getLastCreatedUserDetails();
}
