package com.cmlteam.cmltemplate.security.controller;

import com.cmlteam.cmltemplate.core.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SampleController {
  private final SampleService sampleService;
  @GetMapping("test")
  public String getTest() {
    return sampleService.getTest();
  }
}
