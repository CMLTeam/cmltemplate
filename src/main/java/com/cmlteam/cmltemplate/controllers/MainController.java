package com.cmlteam.cmltemplate.controllers;

import com.cmlteam.cmltemplate.model.TestWsUser;
import com.cmlteam.cmltemplate.services.SampleService;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MainController {

  private final SampleService sampleService;

  @ApiOperation(value = "Test GET endpoint")
  @GetMapping(value = "test")
  public String test() {
    return "Hello CML!";
  }

  @GetMapping(value = "testws")
  public String testws() {
    RestTemplate template = new RestTemplate();
    TestWsUser status =
        Objects.requireNonNull(
            template.getForObject(
                "https://jsonplaceholder.typicode.com/users/1", TestWsUser.class));
    return "" + status.getId();
  }

  @ApiOperation(value = "Show the DB version")
  @GetMapping(value = "testdb")
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

  @ApiOperation(value = "Test POST endpoint with validation")
  @PostMapping(value = "/user")
  public UserResp testPost(@RequestBody @Valid UserReq userReq) {
    return new UserResp(true);
  }
}
