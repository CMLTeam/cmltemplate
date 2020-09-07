package com.cmlteam.cmltemplate.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender javaMailSender;

  @SneakyThrows
  public void sendEmail(String from, String to, String subject, String body, boolean html) {
    if (html) {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      mimeMessage.setSubject(subject);
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
      helper.setFrom(from);
      helper.setTo(to);
      helper.setText(body, true);
      javaMailSender.send(mimeMessage);
    } else {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(from);
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);
      javaMailSender.send(message);
    }
  }

  @PostConstruct
  public void test() {
    log.info("Send test email...");
    String d = "cmlteam.com";
    sendEmail(
        "no-reply@" + d, "volodymyr@" + d, "subj", "body 111 <h1>Header</h1> <b>Bold</b>", true);
    log.info("Sent.");
  }
}
