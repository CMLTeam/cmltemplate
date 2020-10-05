package com.cmlteam.cmltemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
public class TestController {
  @GetMapping("ok")
  @ResponseBody
  public String test() {
    return "OK";
  }
}
