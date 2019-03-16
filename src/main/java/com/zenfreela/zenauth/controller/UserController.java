package com.zenfreela.zenauth.controller;

import com.zenfreela.zenauth.model.User;
import com.zenfreela.zenauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<User> login(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> found = userRepository.findById(user.getEmail());

        return found.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> found = userRepository.findById(user.getEmail());

        if (found.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        userRepository.save(user);
        return ResponseEntity.ok(found.get());
    }

}