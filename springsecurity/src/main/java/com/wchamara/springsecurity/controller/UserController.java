package com.wchamara.springsecurity.controller;

import com.wchamara.springsecurity.model.Customer;
import com.wchamara.springsecurity.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {

        try {
            String hashedPassword = passwordEncoder.encode(customer.getPwd());

            customer.setPwd(hashedPassword);
            Customer saved = customerRepository.save(customer);

            if (saved.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User registration Successful");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred" + e.getMessage());
        }


    }
}
