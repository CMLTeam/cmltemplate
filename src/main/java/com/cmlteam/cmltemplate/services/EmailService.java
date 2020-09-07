package com.cmlteam.cmltemplate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender emailSender;

  public void sendSimpleMessage(String from, String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    emailSender.send(message);
  }

  /*  @PostConstruct
  public void test() {
    log.info("Send test email...");
    String d = "cmlteam.com";
    sendSimpleMessage("no-reply@" + d, "volodymyr@" + d, "subj", "body 111");
    log.info("Sent.");
  }*/
}
