package com.cmlteam.cmltemplate.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 * https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation
 * https://www.baeldung.com/java-aes-encryption-decryption
 * https://www.baeldung.com/java-cipher-class
 */
@RequiredArgsConstructor
public class AesHelper {
  private static final String transformation = "AES/CBC/PKCS5Padding";
  private static final String algorithm = "AES";

  private final byte[] keyBytes;

  @SneakyThrows
  public byte[] encrypt(byte[] message) {
    return cipher(message, Cipher.ENCRYPT_MODE);
  }

  @SneakyThrows
  public byte[] decrypt(byte[] encryptedMessage) {
    return cipher(encryptedMessage, Cipher.DECRYPT_MODE);
  }

  @SneakyThrows
  private byte[] cipher(byte[] message, int opmode) {
    Cipher cipher = Cipher.getInstance(transformation);
    SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);
    cipher.init(opmode, secretKey);
    return cipher.doFinal(message);
  }
}
