package tech.cgnexus.abcbank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    private static final Logger log = LogManager.getLogger(ContactController.class);

    @GetMapping("/contact")
    public String getContact() {
        String apiURL = "http://localhost:8080";
        log.info("Contact API is called. API URL: {}/contact", apiURL);
        return "Contact us at 1800-123-4567";
    }
}
