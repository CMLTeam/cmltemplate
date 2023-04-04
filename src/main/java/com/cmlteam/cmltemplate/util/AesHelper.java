package com.cmlteam.cmltemplate.util;

import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * CBC mode requires a random IV for each encryption operation, you should generate this using
 * SecureRandom each time you encrypt, and pass the value in as an IvParameterSpec. You'll need the
 * same IV for decryption. It is common to prepend the IV to the ciphertext and retrieve when
 * required.
 *
 * <ul>
 *   <li>https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 *   <li>https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation
 *   <li>https://www.baeldung.com/java-aes-encryption-decryption
 *   <li>https://www.baeldung.com/java-cipher-class
 * </ul>
 */
@RequiredArgsConstructor
public class AesHelper {
  private static final String transformation = "AES/CBC/PKCS5Padding";
  private static final String algorithm = "AES";
  private static final SecureRandom secureRandom = new SecureRandom();

  private final byte[] keyBytes;

  @SneakyThrows
  public byte[] encrypt(byte[] message) {
    Cipher cipher = Cipher.getInstance(transformation);
    SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);

    byte[] iv = new byte[16];
    secureRandom.nextBytes(iv);

    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

    byte[] result = new byte[message.length + 16];
    System.arraycopy(iv, 0, result, 0, 16);

    cipher.doFinal(message, 0, message.length, result, 16);

    return result;
  }

  @SneakyThrows
  public byte[] decrypt(byte[] encryptedMessage) {
    Cipher cipher = Cipher.getInstance(transformation);
    SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);

    byte[] iv = new byte[16];
    System.arraycopy(encryptedMessage, 0, iv, 0, 16);

    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    return cipher.doFinal(encryptedMessage, 16, encryptedMessage.length - 16);
  }
}
