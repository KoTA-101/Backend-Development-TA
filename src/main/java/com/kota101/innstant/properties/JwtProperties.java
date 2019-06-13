package com.kota101.innstant.properties;

import lombok.Getter;

@Getter
public class JwtProperties {
    private final String SECRET = "3s6v9y$B&E)H+MbQeThWmZq4t7w!z%C*F-JaNcRfUjXn2r5u8x/A?D(G+KbPeSgVkYp3s6v9y$B&E)H@McQfTjWmZq4t7w!z%C*F-JaNdRgUkXp2r5u8x/A?D(G+KbPe";
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String TOKEN_TYPE = "JWT";
    private final String TOKEN_ISSUER = "secure-api";
    private final String TOKEN_AUDIENCE = "secure-app";
}
