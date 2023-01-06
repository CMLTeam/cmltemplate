package com.cmlteam.cmltemplate.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender javaMailSender;

  @SneakyThrows
  public void sendEmail(
      String from, String to, String subject, String body, boolean html, File attachment) {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    mimeMessage.setSubject(subject);

    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

    helper.setFrom(from);
    helper.setTo(to);
    helper.setText(body, html);

    if (attachment != null) {
      helper.addAttachment(attachment.getName(), attachment);
    }

    javaMailSender.send(mimeMessage);
  }

/*  @PostConstruct
  public void test() {
    log.info("Send test email...");
    String d = "cmlteam.com";
//    sendEmail(
//        "no-reply@" + d,
//        "volodymyr@" + d,
//        "subj",
//        "body 111 <h1>Header</h1> <b>Bold</b>",
//        true,
//        new File("/home/xonix/Desktop/test2.jpg"));
    sendEmail(
        "no-reply@" + d,
        "volodymyr@" + d,
        "subj",
        "body 111 <h1>Header</h1> <b>Bold</b>",
        false,
        null);
    log.info("Sent.");
  }*/
}
