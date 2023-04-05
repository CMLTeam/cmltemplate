package com.cmlteam.cmltemplate.demo.validation;

import static com.cmlteam.cmltemplate.util.JsonUtil.json;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cmlteam.cmltemplate.config.BadRequestErrorRenderer;
import com.cmlteam.cmltemplate.demo.DemoApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DemoApi.class)
@ContextConfiguration(classes = {UserApi.class, BadRequestErrorRenderer.class})
public class BadRequestTests {
  @Autowired private MockMvc mockMvc;

  @Test
  void correctTest() throws Exception {
    mockMvc
        .perform(
            post("/user")
                .header("Content-Type", "application/json")
                .content(json().add("name", "John Smith").add("age", 42).toString()))
        .andExpect(status().isOk());
  }

  @Test
  void badRequestTest() throws Exception {
    mockMvc
        .perform(
            post("/user")
                .header("Content-Type", "application/json")
                .content(json().add("name", "").add("age", -42).toString()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.*", hasSize(2)))
        .andExpect(jsonPath("$.errors.name").value("must not be blank"))
        .andExpect(jsonPath("$.errors.age").value("must be greater than 0"));
  }
}
