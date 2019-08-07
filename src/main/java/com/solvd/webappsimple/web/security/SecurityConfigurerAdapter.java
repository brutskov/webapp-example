package com.solvd.webappsimple.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@EnableWebSecurity
public class SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_API_PATTERNS = new String[] {

    };

    private static final String[] AUTHENTICATED_API_PATTERNS = new String[] {

    };

    private final UserPassAuthService userPassAuthService;
    private final CorsFilter corsFilter;
    private final AuthFilter authFilter;

    public SecurityConfigurerAdapter(UserPassAuthService userPassAuthService,
                                     CorsFilter corsFilter,
                                     AuthFilter authFilter
    ) {
        this.userPassAuthService = userPassAuthService;
        this.corsFilter = corsFilter;
        this.authFilter = authFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                .addFilterAfter(authFilter, ExceptionTranslationFilter.class)
                .authorizeRequests()
                .antMatchers(PUBLIC_API_PATTERNS).permitAll()
                .antMatchers(AUTHENTICATED_API_PATTERNS).authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userPassAuthService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new com.solvd.webappsimple.web.security.PasswordEncoder();
    }

}
