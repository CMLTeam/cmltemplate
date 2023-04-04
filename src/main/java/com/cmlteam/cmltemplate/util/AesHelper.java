package com.cmlteam.cmltemplate.util;

import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
public class AesHelper {
  private static final String transformation = "AES/CBC/PKCS5Padding";
  private static final String algorithm = "AES";
  private static final int keySize = 128;
  private static final int ivSize = 16;
  private static final SecureRandom secureRandom = new SecureRandom();

  private final byte[] key;

  public AesHelper(byte[] key) {
    if (key.length != keySize / 8) {
      throw new IllegalArgumentException("Invalid key size");
    }
    this.key = key;
  }

  @SneakyThrows
  public byte[] encrypt(byte[] message) {
    SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
    Cipher cipher = Cipher.getInstance(transformation);
    byte[] iv = new byte[ivSize];
    secureRandom.nextBytes(iv);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
    byte[] encrypted = cipher.doFinal(message);
    byte[] result = new byte[ivSize + encrypted.length];
    System.arraycopy(iv, 0, result, 0, ivSize);
    System.arraycopy(encrypted, 0, result, ivSize, encrypted.length);
    return result;
  }

  @SneakyThrows
  public byte[] decrypt(byte[] encryptedMessage) {
    SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
    Cipher cipher = Cipher.getInstance(transformation);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(encryptedMessage, 0, ivSize));
    return cipher.doFinal(encryptedMessage, ivSize, encryptedMessage.length - ivSize);
  }
}
