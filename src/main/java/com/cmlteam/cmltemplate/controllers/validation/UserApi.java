package com.cmlteam.cmltemplate.controllers.validation;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserApi {
  @ApiOperation(value = "Test POST endpoint with validation")
  @PostMapping(value = "/user")
  public UserResp testPost(@Valid @RequestBody UserReq userReq) {
    return new UserResp(true);
  }
}
