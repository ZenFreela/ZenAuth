package com.zenfreela.zenauth.controller;

import com.zenfreela.zenauth.model.User;
import com.zenfreela.zenauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserController(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<User> login(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> found = userRepository.findById(user.getEmail());

        if (!found.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User find = found.get();
        String password = passwordEncoder.encode(find.getPassword());

        if (!passwordEncoder.matches(user.getPassword(), password)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(find);
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

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok(found.get());
    }

}