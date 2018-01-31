package com.cmlteam.cmltemplate.controllers;

import com.cmlteam.cmltemplate.model.ServerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController() {
    }

    @RequestMapping(
            value = "/webhook",
            method = RequestMethod.GET)
    public String getWebHook() {
        RestTemplate template = new RestTemplate();
        ServerStatus status = template.getForObject("https://l2c1x1.com/services/misc/server-stats", ServerStatus.class);
        return "" + status.totalAccounts;
    }
}
