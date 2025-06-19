package com.example.springrest_refresher.restclient.clockwebapp.config;

import com.example.springrest_refresher.restclient.clockwebapp.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    MyUserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, @Qualifier("restClientAuthProvider") AuthenticationProvider authenticationProvider) throws Exception {
        httpSecurity.securityMatcher("/webapp/**", "/oauth2/**")
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/webapp/login").permitAll()
                                .anyRequest().authenticated()
                        // The following code will restrict users based on their roles.
                        // But I have commented this, because if try to log in through oauth2, it will result in 403 Forbidden error.
//                        .requestMatchers("/webapp").hasRole("VIEW_DATETIME")
//                        .requestMatchers("/webapp/set-datetime", "/webapp/set-subscribed-timezones").hasRole("UPDATE_DATETIME")
                ).formLogin(form ->
                        form.loginPage("/webapp/login")
                                .loginProcessingUrl("/webapp/login")
                                .defaultSuccessUrl("/webapp?autoRefresh=true", true)
                                .permitAll()
                ).oauth2Login((oauth) -> oauth.loginPage("/webapp/login") //Note: Session management will not work for oauth2, However, we can write custom code for that mechanism if needed.
                        .defaultSuccessUrl("/webapp?autoRefresh=true", true)
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/webapp/login/oauth2/code/*"))
                        .permitAll()
                ).logout(logout ->
                        logout.logoutUrl("/webapp/logout")
                                .logoutSuccessUrl("/webapp/login")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                ).sessionManagement(session -> session.maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                ).authenticationProvider(authenticationProvider);

        return httpSecurity.build();
    }

    @Bean("restClientAuthProvider")
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    // Required for session management
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
