package tech.cgnexus.abcbank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {

    private static final Logger log = LogManager.getLogger(CardsController.class);

    @GetMapping("/my-cards")
    public String getCardsDetails() {
        String apiURL = "http://localhost:8080";
        log.info("Cards details API is called. API URL: {}/my-cards", apiURL);
        return "here are your cards details";
    }
}
