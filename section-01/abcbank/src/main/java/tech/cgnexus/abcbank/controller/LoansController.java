package tech.cgnexus.abcbank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    private static final Logger log = LogManager.getLogger(LoansController.class);

    @GetMapping("/my-loans")
    public String getLoansDetails() {
        String apiURL = "http://localhost:8080";
        log.info("Loans details API is called. API URL: {}/my-loans", apiURL);
        return "here are your loans details";
    }
}
