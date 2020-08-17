package kz.danke.test.project.configuration.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.danke.test.project.configuration.security.CustomUserDetails;
import kz.danke.test.project.configuration.security.token.TokenService;
import kz.danke.test.project.dto.request.LoginRequest;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment environment;
    private final TokenService tokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Environment environment,
                                TokenService tokenService) {
        this.environment = environment;
        this.tokenService = tokenService;
        super.setAuthenticationManager(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = new ObjectMapper()
                .readValue(request.getInputStream(), LoginRequest.class);

        return getAuthenticationManager()
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                        )
                );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails principal = (CustomUserDetails) authResult.getPrincipal();

        String token = tokenService.generateToken(principal);

        response.addHeader("Authorization", token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
