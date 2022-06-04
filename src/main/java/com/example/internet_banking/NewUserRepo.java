package com.example.internet_banking;

import org.springframework.data.repository.CrudRepository;

public interface NewUserRepo extends CrudRepository<NewUserDetails , Long> {
}
