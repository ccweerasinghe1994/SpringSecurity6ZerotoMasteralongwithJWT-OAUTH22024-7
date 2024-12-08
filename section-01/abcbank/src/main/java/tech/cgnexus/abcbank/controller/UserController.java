package tech.cgnexus.abcbank.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.cgnexus.abcbank.model.Customer;
import tech.cgnexus.abcbank.repository.CustomerRepository;

@RestController
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashedPassword = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashedPassword);
            Customer savedCustomer = customerRepository.save(customer);

            if (savedCustomer.getId() > 0) {
                log.info("User registered successfully: {}", savedCustomer.getEmail());
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully: " + savedCustomer.getEmail());
            } else {
                log.error("Error occurred while saving user to database: {}", customer.getEmail());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving user: " + customer.getEmail());
            }

        } catch (Exception e) {
            log.error("Error occurred while registering user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while registering user: " + e.getMessage());
        }
    }
}
