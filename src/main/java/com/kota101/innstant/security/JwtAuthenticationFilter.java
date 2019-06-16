package com.kota101.innstant.security;

import com.kota101.innstant.data.repository.UserRepository;
import com.kota101.innstant.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private CryptoGenerator cryptoGenerator = new CryptoGenerator();
    private final JwtProperties properties = new JwtProperties();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/users/authenticate");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            String[] value = new String(Base64.getDecoder().decode(authorization.substring("Basic".length()).trim())).split(":", 2);
            String email = value[0];
            String password = value[1];
            if (email != null && password != null) {
                if (cryptoGenerator.verifyHash(password, Objects.requireNonNull(userRepository.findByEmail(email).block()).getPassword())) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
                    return authenticationManager.authenticate(authenticationToken);
                }
            }
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) {
        User user = getPrincipal(authentication);
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

    private User getPrincipal(@AuthenticationPrincipal Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
