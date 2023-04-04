package com.cmlteam.cmltemplate;

import com.cmlteam.cmltemplate.controllers.MainController;
import com.cmlteam.cmltemplate.services.SampleService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MainController.class)
@ContextConfiguration(classes = {SampleService.class, MainController.class})
public class BadRequestTests {

}
