package com.cmlteam.cmltemplate.controllers;

import com.cmlteam.cmltemplate.model.ServerStatus;
import com.cmlteam.cmltemplate.services.SampleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MainController {

  private final SampleService sampleService;

  @Autowired
  public MainController(SampleService sampleService) {
    this.sampleService = sampleService;
  }

  @ApiOperation(value = "Test GET endpoint")
  @GetMapping
  public String test() {
    return "Hello CML!";
  }

  @GetMapping("testws")
  public String testws() {
    RestTemplate template = new RestTemplate();
    ServerStatus status =
        template.getForObject("https://l2c1x1.com/services/misc/server-stats", ServerStatus.class);
    return "" + status.totalAccounts;
  }

  @ApiOperation(value = "Show the DB version")
  @GetMapping("testdb")
  public String testdb() {
    return sampleService.getDbVersion();
  }

  @ApiOperation(value = "Test POST endpoint")
  @PostMapping(value = "/testpost")
  public Map testPost(@RequestBody Map payload) {
    log.info("Test POST: {}", payload);
    HashMap res = new HashMap();
    res.put("success", true);
    return res;
  }
}
