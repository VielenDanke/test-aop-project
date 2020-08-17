package kz.danke.test.project.configuration.security.token.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kz.danke.test.project.configuration.security.CustomUserDetails;
import kz.danke.test.project.configuration.security.token.TokenService;
import kz.danke.test.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    private final Environment environment;

    @Autowired
    public TokenServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String generateToken(CustomUserDetails principal) {
        Date issuedDate = new Date();

        Date expirationDate = new Date(issuedDate.getTime() + Long.parseLong(
                Objects.requireNonNull(environment.getProperty("token.expiration"))
        ));

        return Jwts.builder()
                .setClaims(this.generateClaims(principal))
                .setSubject(principal.getUser().getId().toString())
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(
                        Keys.hmacShaKeyFor(Objects.requireNonNull(environment.getProperty("token.secret")).getBytes()),
                        SignatureAlgorithm.HS512
                )
                .compact();

    }

    @Override
    public boolean validateToken(String token) {
        return getClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    @Override
    public Claims getTokenClaims(String token) {
        return getClaimsJws(token)
                .getBody();
    }

    @Override
    public String getTokenSubject(String token) {
        return getClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Map<String, Object> generateClaims(CustomUserDetails principal) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining("/")));

        return claims;
    }

    private Jws<Claims> getClaimsJws(String token) {
        String secretKey = Base64.getEncoder().encodeToString(
                Objects.requireNonNull(environment.getProperty("token.secret")).getBytes()
        );

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}
