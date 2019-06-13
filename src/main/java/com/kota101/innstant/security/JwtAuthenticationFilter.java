package com.kota101.innstant.security;

import com.kota101.innstant.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private final JwtProperties properties = new JwtProperties();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/users/authenticate");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = getRequestBodyParams(request);
        String username = null;
        String password = null;
        try {
            Objects.requireNonNull(jsonObject);
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) {
        User user = ((User) authentication.getPrincipal());
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(properties.getSECRET().getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", properties.getTOKEN_TYPE())
                .setIssuer(properties.getTOKEN_ISSUER())
                .setAudience(properties.getTOKEN_AUDIENCE())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .claim("role", roles)
                .compact();
        response.addHeader(properties.getTOKEN_HEADER(), properties.getTOKEN_PREFIX() + token);
    }

    private JSONObject getRequestBodyParams(HttpServletRequest request) {
        BufferedReader reader;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = request.getReader();
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
