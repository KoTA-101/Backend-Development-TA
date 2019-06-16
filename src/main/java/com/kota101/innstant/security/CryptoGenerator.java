package com.kota101.innstant.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CryptoGenerator {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String generateHash(String plainText) {
        return encoder.encode(plainText);
    }

    boolean verifyHash(String plainText, String hashedPassword) {
        return encoder.matches(plainText, hashedPassword);
    }
}
