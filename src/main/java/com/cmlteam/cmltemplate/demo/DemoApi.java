package com.cmlteam.cmltemplate.demo;

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

@Slf4j
@RequiredArgsConstructor
@RestController
public class DemoApi {

  private final DemoService demoService;

  @ApiOperation(value = "Test GET endpoint")
  @GetMapping(value = "test")
  public String test() {
    return "Hello CML!";
  }

  @GetMapping(value = "testws")
  public String testws() {
    RestTemplate template = new RestTemplate();
    DemoWsUser status =
        Objects.requireNonNull(
            template.getForObject(
                "https://jsonplaceholder.typicode.com/users/1", DemoWsUser.class));
    return "" + status.getId();
  }

  @ApiOperation(value = "Show the DB version")
  @GetMapping(value = "testdb")
  public String testdb() {
    return demoService.getDbVersion();
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
