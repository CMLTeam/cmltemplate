package com.cmlteam.cmltemplate.services;

import com.cmlteam.cmltemplate.config.RabbitConfig;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Sample service to demo RabbitMQ. */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty({"rabbitmq.enabled"})
public class RabbitService {
  private final RabbitTemplate rabbitTemplate;

  @PostConstruct
  public void doAtStartup() {
    rabbitTemplate.convertAndSend("simple_exchange", "routing_1", "HELLO RABBIT !!!");
  }

  @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
  public void listen(String in) {
    log.info("!!! Message received from rabbit queue : " + in);
  }
}
