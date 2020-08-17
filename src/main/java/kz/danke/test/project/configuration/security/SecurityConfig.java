package kz.danke.test.project.configuration.security;

import kz.danke.test.project.configuration.security.filter.AuthenticationFilter;
import kz.danke.test.project.configuration.security.filter.AuthorizationFilter;
import kz.danke.test.project.configuration.security.token.TokenService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final Environment environment;
    private final TokenService tokenService;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder,
                          @Qualifier("userServiceImpl") UserDetailsService userDetailsService,
                          Environment environment,
                          TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.environment = environment;
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();
        http
                .headers()
                .frameOptions()
                .disable();
        http
                .httpBasic()
                .disable();
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/courses**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(getAuthorizationFilter());
    }

    @SneakyThrows
    @Bean
    public AuthenticationFilter getAuthenticationFilter() {
        final String filterProcessesUrl = "/login";

        AuthenticationFilter filter = new AuthenticationFilter(
                authenticationManager(), environment, tokenService
        );

        filter.setFilterProcessesUrl(filterProcessesUrl);

        return filter;
    }

    @SneakyThrows
    @Bean
    public AuthorizationFilter getAuthorizationFilter() {
        return new AuthorizationFilter(authenticationManager(), environment, tokenService);
    }
}
