package com.cmlteam.cmltemplate;

import com.cmlteam.cmltemplate.controllers.MainController;
import com.cmlteam.cmltemplate.services.SampleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private SampleService service;

  @MockBean private DataSource dataSource;

  @Before
  public void setup() {
    when(service.getDbVersion()).thenReturn("TEST_DB");
  }

  @Test
  public void simpleGetTest() throws Exception {
    mockMvc
        .perform(get("/test"))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("Hello CML!")));
  }

  @Test
  public void simpleWsTest() throws Exception {
    mockMvc
        .perform(get("/testws"))
        .andExpect(status().isOk())
        .andExpect(content().string(matchesPattern("\\d+")));
  }

  @Test
  public void simplePostTest() throws Exception {
    mockMvc
        .perform(
            post("/testpost")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"payload\": {\"test\": \"test\"}}"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"success\": true}"));
  }
}
