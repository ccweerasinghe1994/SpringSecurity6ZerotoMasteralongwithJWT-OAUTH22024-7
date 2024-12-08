package tech.cgnexus.abcbank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {

    private static final Logger log = LogManager.getLogger(NoticesController.class);

    @GetMapping("/notices")
    public String getNotices() {
        String apiURL = "http://localhost:8080";
        log.info("Notices API is called. API URL: {}/notices", apiURL);
        return "here are the notices";
    }
}
