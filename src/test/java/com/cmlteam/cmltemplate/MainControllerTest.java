package com.cmlteam.cmltemplate;

import com.cmlteam.cmltemplate.controllers.MainController;
import com.cmlteam.cmltemplate.services.SampleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private SampleService service;

  @MockBean private DataSource dataSource;

  @Test
  public void simpleTest() throws Exception {
    when(service.getDbVersion()).thenReturn("TEST_DB");
    mockMvc.perform(get("/testws")).andExpect(status().isOk()).andExpect(content().string("2669"));
  }
}
