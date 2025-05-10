package org.itsci.senior_connect.service;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class PasswordUtil {

    private static final int DEFAULT_SIZE = 32;

    // สร้าง Salt ใหม่
    public String createSalt() throws NoSuchAlgorithmException {
        return createBase64Salt(32);
    }

    // สร้าง Salt แบบ Base64
    private String createBase64Salt(int size) throws NoSuchAlgorithmException {
        return java.util.Base64.getEncoder().encodeToString(getSalt(size));
    }

    // สร้าง Salt ตามขนาดที่ต้องการ
    private byte[] getSalt(int size) throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        final byte[] salt = new byte[size < DEFAULT_SIZE ? DEFAULT_SIZE : size];
        sr.nextBytes(salt);
        return salt;
    }

    // สร้างรหัสผ่านจาก Salt ใหม่
    public String createPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return sha256(password.concat(createSalt()));
    }

    // สร้างรหัสผ่านโดยใช้ Salt ที่ให้มา
    public String createPassword(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return sha256(password.concat(salt));
    }

    // ฟังก์ชันสำหรับการเข้ารหัสด้วย SHA-256
    public String sha256(String base) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

