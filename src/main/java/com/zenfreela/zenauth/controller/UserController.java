package com.zenfreela.zenauth.controller;

import com.zenfreela.zenauth.model.User;
import com.zenfreela.zenauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<Flux<User>> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping(path = "/", produces = "application/json")
    public Mono<Void> deleteAll() {
        return userRepository.deleteAll();
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<Mono<User>> login(@RequestBody User body) {
        if (body == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(body.getEmail()).block();

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(Mono.just(user));
    }

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<Mono<User>> register(@RequestBody User body) {
        if (body == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(body.getEmail()).block();

        if (user != null) {
            return ResponseEntity.badRequest().build();
        }

        body.setPassword(passwordEncoder.encode(body.getPassword()));

        return ResponseEntity.ok(userRepository.save(body));
    }

}