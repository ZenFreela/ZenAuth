package com.zenfreela.zenauth.repository;

import com.zenfreela.zenauth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {}