package com.cmlteam.cmltemplate;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages = "com.cmlteam")
public class Application {

  @Autowired
  RabbitTemplate rabbitTemplate;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  public void doAtStartup() {
    rabbitTemplate.convertAndSend("simple_exchange", "routing_1", "HELLO RABBIT !!!");
    // Insert here post-start action if need be
  }
}
