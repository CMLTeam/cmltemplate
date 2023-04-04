package com.cmlteam.cmltemplate.util;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AesHelperTests {
  @Test
  void test1() {
    // GIVEN
    AesHelper aesHelper = new AesHelper("key-must-be-16-b".getBytes(StandardCharsets.UTF_8));
    String input = "hello world 123";
    byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

    // WHEN
    byte[] encrypted = aesHelper.encrypt(inputBytes);
    byte[] result = aesHelper.decrypt(encrypted);

    // THEN
    Assertions.assertEquals(input, new String(result, StandardCharsets.UTF_8));
  }
}
