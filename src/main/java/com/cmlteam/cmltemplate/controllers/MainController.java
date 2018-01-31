package com.cmlteam.cmltemplate.controllers;

import com.cmlteam.cmltemplate.model.ServerStatus;
import com.cmlteam.cmltemplate.services.SampleService;
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

    private final SampleService sampleService;

    @Autowired
    public MainController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String test() {
        return "Hello CML!";
    }
    @RequestMapping(value = "/testws", method = RequestMethod.GET)
    public String testws() {
        RestTemplate template = new RestTemplate();
        ServerStatus status = template.getForObject("https://l2c1x1.com/services/misc/server-stats", ServerStatus.class);
        return "" + status.totalAccounts;
    }
    @RequestMapping(value = "/testdb", method = RequestMethod.GET)
    public String testdb() {
        return sampleService.getDbVersion();
    }
}
