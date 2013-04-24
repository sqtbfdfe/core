 package org.liuz.core.security.utils;

 import java.security.GeneralSecurityException;
 import java.util.Arrays;
 import javax.crypto.Cipher;
 import javax.crypto.KeyGenerator;
 import javax.crypto.Mac;
 import javax.crypto.SecretKey;
 import javax.crypto.SecretKeyFactory;
 import javax.crypto.spec.DESKeySpec;
 import javax.crypto.spec.SecretKeySpec;
 import org.liuz.core.utils.EncodeUtils;

 public class CryptoUtils
 {
   private static final String DES = "DES";
   private static final String AES = "AES";
   private static final String HMACSHA1 = "HmacSHA1";
   private static final int DEFAULT_HMACSHA1_KEYSIZE = 160;
   private static final int DEFAULT_AES_KEYSIZE = 128;

   public static byte[] hmacSha1(String input, byte[] keyBytes)
   {
     try
     {
       SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
       Mac mac = Mac.getInstance("HmacSHA1");
       mac.init(secretKey);
       return mac.doFinal(input.getBytes());
     } catch (GeneralSecurityException e) {
       throw new IllegalStateException("Security exception", e);
     }
   }

   public static String hmacSha1ToHex(String input, byte[] keyBytes)
   {
     byte[] macResult = hmacSha1(input, keyBytes);
     return EncodeUtils.hexEncode(macResult);
   }

   public static String hmacSha1ToBase64(String input, byte[] keyBytes)
   {
     byte[] macResult = hmacSha1(input, keyBytes);
     return EncodeUtils.base64Encode(macResult);
   }

   public static String hmacSha1ToBase64UrlSafe(String input, byte[] keyBytes)
   {
     byte[] macResult = hmacSha1(input, keyBytes);
     return EncodeUtils.base64UrlSafeEncode(macResult);
   }

   public static boolean isHexMacValid(String hexMac, String input, byte[] keyBytes)
   {
     byte[] expected = EncodeUtils.hexDecode(hexMac);
     byte[] actual = hmacSha1(input, keyBytes);

     return Arrays.equals(expected, actual);
   }

   public static boolean isBase64MacValid(String base64Mac, String input, byte[] keyBytes)
   {
     byte[] expected = EncodeUtils.base64Decode(base64Mac);
     byte[] actual = hmacSha1(input, keyBytes);

     return Arrays.equals(expected, actual);
   }

   public static byte[] generateMacSha1Key()
   {
     try
     {
       KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
       keyGenerator.init(160);
       SecretKey secretKey = keyGenerator.generateKey();
       return secretKey.getEncoded();
     } catch (GeneralSecurityException e) {
       throw new IllegalStateException("Security exception", e);
     }
   }

   public static String generateMacSha1HexKey()
   {
     return EncodeUtils.hexEncode(generateMacSha1Key());
   }

   public static String desEncryptToHex(String input, byte[] keyBytes)
   {
     byte[] encryptResult = des(input.getBytes(), keyBytes, 1);
     return EncodeUtils.hexEncode(encryptResult);
   }

   public static String desEncryptToBase64(String input, byte[] keyBytes)
   {
     byte[] encryptResult = des(input.getBytes(), keyBytes, 1);
     return EncodeUtils.base64Encode(encryptResult);
   }

   public static String desDecryptFromHex(String input, byte[] keyBytes)
   {
     byte[] decryptResult = des(EncodeUtils.hexDecode(input), keyBytes, 2);
     return new String(decryptResult);
   }

   public static String desDecryptFromBase64(String input, byte[] keyBytes)
   {
     byte[] decryptResult = des(EncodeUtils.base64Decode(input), keyBytes, 2);
     return new String(decryptResult);
   }

   private static byte[] des(byte[] inputBytes, byte[] keyBytes, int mode)
   {
     try
     {
       DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
       SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
       SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

       Cipher cipher = Cipher.getInstance("DES");
       cipher.init(mode, secretKey);
       return cipher.doFinal(inputBytes);
     } catch (GeneralSecurityException e) {
       throw new IllegalStateException("Security exception", e);
     }
   }

   public static byte[] generateDesKey()
   {
     try
     {
       KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
       SecretKey secretKey = keyGenerator.generateKey();
       return secretKey.getEncoded();
     } catch (GeneralSecurityException e) {
       throw new IllegalStateException("Security exception", e);
     }
   }

   public static String generateDesHexKey()
   {
     return EncodeUtils.hexEncode(generateDesKey());
   }

   public static String aesEncryptToHex(String input, byte[] keyBytes)
   {
     byte[] encryptResult = aes(input.getBytes(), keyBytes, 1);
     return EncodeUtils.hexEncode(encryptResult);
   }

   public static String aesEncryptToBase64(String input, byte[] keyBytes)
   {
     byte[] encryptResult = aes(input.getBytes(), keyBytes, 1);
     return EncodeUtils.base64Encode(encryptResult);
   }

   public static String aesDecryptFromHex(String input, byte[] keyBytes)
   {
     byte[] decryptResult = aes(EncodeUtils.hexDecode(input), keyBytes, 2);
     return new String(decryptResult);
   }

   public static String aesDecryptFromBase64(String input, byte[] keyBytes)
   {
     byte[] decryptResult = aes(EncodeUtils.base64Decode(input), keyBytes, 2);
     return new String(decryptResult);
   }

   private static byte[] aes(byte[] inputBytes, byte[] keyBytes, int mode)
   {
     try
     {
       SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
       Cipher cipher = Cipher.getInstance("AES");
       cipher.init(mode, secretKey);
       return cipher.doFinal(inputBytes);
     } catch (Exception e) {
       throw new IllegalStateException("Security exception", e);
     }
   }

   public static byte[] generateAesKey()
   {
     try
     {
       KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
       keyGenerator.init(128);
       SecretKey secretKey = keyGenerator.generateKey();
       return secretKey.getEncoded();
     } catch (GeneralSecurityException e) {
       throw new IllegalStateException("Security exception", e);
     }
   }

   public static String generateAesHexKey()
   {
     return EncodeUtils.hexEncode(generateAesKey());
   }
 }

