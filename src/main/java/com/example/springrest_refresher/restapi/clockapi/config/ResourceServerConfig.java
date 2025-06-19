package com.example.springrest_refresher.restapi.clockapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher("/api/**", "/swagger-ui/**")
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/set-datetime", "/api/set-datetime-from-timezone", "/api/reset-datetime")
                        .access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasAuthority("SCOPE_WRITE_DATETIME")))
                        .requestMatchers("/api/get-datetime", "/api/datetime-on", "/api/datetime-at")
                        .access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasAuthority("SCOPE_READ_DATETIME")))
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
