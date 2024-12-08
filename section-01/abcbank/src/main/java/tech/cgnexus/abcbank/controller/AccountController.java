package tech.cgnexus.abcbank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private static final Logger log = LogManager.getLogger(AccountController.class);

    @GetMapping("/my-account")
    public String getAccountDetails() {
        String apiURL = "http://localhost:8080";
        log.info("Account details API is called. API URL: {}/my-account", apiURL);
        return "here are your account details";
    }
}
