package com.zenfreela.zenauth.repository;

import com.zenfreela.zenauth.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {}