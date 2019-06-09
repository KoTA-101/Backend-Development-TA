package com.kota101.innstant.security;

import com.kota101.innstant.constant.SecurityConstants;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
public class CryptoGenerator {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private SecurityConstants properties = new SecurityConstants();

    public String generateHashedPassword(String password) {
        return encoder.encode(password);
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }

    public BigInteger generateHashedPin(BigInteger pin) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(properties.getAES_IV().getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(properties.getAES_KEY().getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedPin = cipher.doFinal(pin.toByteArray());
            return new BigInteger(encryptedPin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifyPin(BigInteger pin, BigInteger hashedPin) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(properties.getAES_IV().getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(properties.getAES_KEY().getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            if (Arrays.equals(pin.toByteArray(), cipher.doFinal(hashedPin.toByteArray()))) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
