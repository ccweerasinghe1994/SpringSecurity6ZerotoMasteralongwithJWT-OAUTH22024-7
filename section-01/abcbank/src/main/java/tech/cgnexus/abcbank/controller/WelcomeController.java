package tech.cgnexus.abcbank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    private static final Logger log = LogManager.getLogger(WelcomeController.class);

    @GetMapping("/welcome")
    public String welcome() {
        String apiURL = "http://localhost:8080";
        log.info("Welcome API is called. API URL: {}/welcome", apiURL);
        return "Welcome to ABC Bank! Your security is our priority.";
    }
}
