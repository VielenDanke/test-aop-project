package kz.danke.test.project.configuration.security.filter;

import io.jsonwebtoken.Claims;
import kz.danke.test.project.configuration.security.token.TokenService;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment environment;
    private final TokenService tokenService;

    public AuthorizationFilter(AuthenticationManager authenticationManager,
                               Environment environment,
                               TokenService tokenService) {
        super(authenticationManager);
        this.environment = environment;
        this.tokenService = tokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(environment.getProperty("token.header.name"));

        if (authorizationHeader == null || !authorizationHeader
                .startsWith(Objects.requireNonNull(environment.getProperty("token.header.prefix")))) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(environment.getProperty("token.header.name"));

        if (authorizationHeader == null) {
            return null;
        }
        String token = authorizationHeader
                .substring(7);

        if (tokenService.validateToken(token)) {
            return null;
        }
        Claims tokenClaims = tokenService.getTokenClaims(token);

        if (tokenClaims == null) {
            return null;
        }
        String joinedRoles = tokenClaims.get("roles", String.class);

        List<SimpleGrantedAuthority> roles = Stream.of(joinedRoles.split("/"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String userId = tokenService.getTokenSubject(token);

        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                roles);
    }
}
