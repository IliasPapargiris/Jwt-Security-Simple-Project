package com.logicea.demo.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration implements WebSecurityConfigurer<WebSecurity> {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final HandlerMappingIntrospector introspector;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .and()
                .cors()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**")
                .permitAll()
                .requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations()))
                .permitAll()
//                "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**
                .requestMatchers("/swagger-ui.html")
                .permitAll()
                .requestMatchers("/configuration/ui")
                .permitAll()
                .requestMatchers("/configuration/security")
                .permitAll()
                .requestMatchers("/webjars/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/cards/allCards").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            String message = accessDeniedException.getMessage();
                            response.getWriter().write("{\"message\": \"" + message + "\"}");
                        })
                )
                .httpBasic()
                .disable()
                .formLogin()
                .disable()
                .logout()
                .disable()
                .csrf()
                .disable()
                .cors()
                .disable()
                .headers()
                .frameOptions()
                .disable();

        return http.build();
    }

    @Override
    public void init(WebSecurity builder) throws Exception {

    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .requestMatchers(new MvcRequestMatcher(introspector, "/public/**"))
                .requestMatchers(new MvcRequestMatcher(introspector, "/swagger-ui/**"))
                .requestMatchers(new MvcRequestMatcher(introspector, "/v2/api-docs"))
                .requestMatchers(new MvcRequestMatcher(introspector, "/webjars/**"));
    }

}

