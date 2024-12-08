package tech.cgnexus.abcbank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    private static final Logger log = LogManager.getLogger(BalanceController.class);

    @GetMapping("/my-balance")
    public String getBalanceDetails() {
        String apiURL = "http://localhost:8080";
        log.info("Balance details API is called. API URL: {}/my-balance", apiURL);
        return "here are your balance details";
    }
}
