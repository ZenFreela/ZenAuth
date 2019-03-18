package com.zenfreela.zenauth.controller;

import com.zenfreela.zenauth.model.User;
import com.zenfreela.zenauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserController(@Autowired UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping(path = "/", produces = "application/json")
    public void deleteAll() {
        userRepository.deleteAll();
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

        if (!passwordEncoder.matches(user.getPassword(), find.getPassword())) {
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

        return ResponseEntity.ok(userRepository.save(user));
    }

}