package com.accrualify.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author Narsi Nallamilli
 * @since 14 Dec 2017
 * <p>
 * Utility to encrypt and decrypt data.
 */
public class AES_Util {
    private static byte[] key;

    private static final SecretKeySpec secretKeySpec = setKey("");
    private static final transient Log log = LogFactory.getLog(AES_Util.class);

    public static void main(String[] args) throws Exception {
        String originalString = "t4pJReEAfJk4Erbdse2QpIp/dqDVrUnqA3GEkxp2Hw4=";
//        String encryptedString = AES_Util.encrypt(originalString)
        String decryptedString = AES_Util.decrypt(originalString);

        System.out.println(originalString);
//        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }

    public static SecretKeySpec setKey(String myKey) {
        SecretKeySpec secretKeySpec = null;
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error Setting SecretKeySpec",e);
        }
        return secretKeySpec;
    }

    public static String cipher(String strToCiper, CryptoType cryptoType) {
        String ciperText = null;
        switch (cryptoType) {
            case ENCRYPT:
                ciperText = strToCiper == null ? null : encrypt(strToCiper);
                break;
            case DECRYPT:
                ciperText = strToCiper == null ? null : decrypt(strToCiper);
                break;
        }
        return ciperText;
    }

    public static String encrypt(String strToEncrypt) {
        String ecodedString = null;
        if (strToEncrypt != null) {
            try {
                //setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                ecodedString = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            } catch (Exception e) {
                log.error("Error while encrypting: ", e);
                throw new RuntimeException("Error eccripting",e);
            }
        }
        return ecodedString;
    }

    public static String decrypt(String strToDecrypt) {
        String decodedString = null;
        if (strToDecrypt != null) {
            try {
                //setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                decodedString = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            } catch (Exception e) {
                log.error("Error while decrypting: ", e);
                throw new RuntimeException("Error deccripting",e);
            }
        }
        return decodedString;
    }

    public enum CryptoType {
        ENCRYPT, DECRYPT;
    }
}