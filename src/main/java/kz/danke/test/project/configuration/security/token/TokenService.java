package kz.danke.test.project.configuration.security.token;

import io.jsonwebtoken.Claims;
import kz.danke.test.project.configuration.security.CustomUserDetails;

import java.util.Map;

public interface TokenService {

    String generateToken(CustomUserDetails principal);

    boolean validateToken(String token);

    Claims getTokenClaims(String token);

    String getTokenSubject(String token);

    Map<String, Object> generateClaims(CustomUserDetails principal);
}
