package kz.danke.test.project.configuration.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.danke.test.project.configuration.security.CustomUserDetails;
import kz.danke.test.project.configuration.security.FilterResponseHandler;
import kz.danke.test.project.configuration.security.token.TokenService;
import kz.danke.test.project.dto.request.LoginRequest;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenService tokenService;

    private FilterResponseHandler filterResponseHandler;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                TokenService tokenService) {
        this.tokenService = tokenService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getUsername(),
                                    loginRequest.getPassword()
                            )
                    );
        } catch (IOException e) {
            final int status = 400;

            log.error(e.getLocalizedMessage(), e);

            filterResponseHandler.handleFilterException(
                    e, request, response, status
            );
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails principal = (CustomUserDetails) authResult.getPrincipal();

        final String token = tokenService.generateToken(principal);
        final String authorizationHeader = "Authorization";

        response.addHeader(authorizationHeader, token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

    public void setFilterResponseHandler(FilterResponseHandler filterResponseHandler) {
        this.filterResponseHandler = filterResponseHandler;
    }
}
