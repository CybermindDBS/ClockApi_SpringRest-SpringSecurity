package com.example.springrest_refresher.authorizationserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    /*
     To get the authorization code visit,
      http://localhost:8080/oauth2/authorize?response_type=code&client_id=postman&redirect_uri=http://someclientofclockapi.com:8081/login/oauth2/code/clock-api-auth-server&scope=openid%20READ_DATETIME%20WRITE_DATETIME&state=xyz
     */

    @Bean
    public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity httpSecurity, @Qualifier("authServerAuthProvider") AuthenticationProvider authenticationProvider) throws Exception {

        OAuth2AuthorizationServerConfigurer auth2AuthorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        httpSecurity.securityMatcher(auth2AuthorizationServerConfigurer.getEndpointsMatcher())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/api-auth/login").permitAll()
                        .loginProcessingUrl("/api-auth/login"))
                .authenticationProvider(authenticationProvider)
                .csrf(csrf -> csrf.ignoringRequestMatchers(auth2AuthorizationServerConfigurer.getEndpointsMatcher()))
                .with(auth2AuthorizationServerConfigurer, configurer -> configurer.oidc(Customizer.withDefaults()));

        return httpSecurity.build();
    }

    // Using this bean since authorizationSecurityFilterChain doesn't work with /login/** endpoint.
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, @Qualifier("authServerAuthProvider") AuthenticationProvider authenticationProvider) throws Exception {
        httpSecurity.securityMatcher("/api-auth/login/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login.loginPage("/api-auth/login").permitAll()
                        .loginProcessingUrl("/api-auth/login"))
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider);

        return httpSecurity.build();
    }

    @Bean("authCodeClientUserDetailsService")
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails userDetails = User.withUsername("user4")
                .password(passwordEncoder.encode("pass123"))
                .roles("VIEW_DATETIME", "UPDATE_DATETIME")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean("authServerAuthProvider")
    public AuthenticationProvider authenticationProvider(@Qualifier("authCodeClientUserDetailsService") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://localhost:8080").build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient clientCredentialsClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("my-clock")
                .clientSecret("{noop}secret1") // passwordEncoder.encode("secret1")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("READ_DATETIME").scope("WRITE_DATETIME")
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1)).build())
                .build();

        RegisteredClient authCodeClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("MyClock")
                .clientSecret("{noop}secret2")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .redirectUri("http://localhost:8080/webapp/login/oauth2/code/my-auth")
                .scope(OidcScopes.OPENID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .build();

        RegisteredClient postmanAuthCodeClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("postman")
                .clientSecret("{noop}secret3")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .redirectUri("http://someclientofclockapi.com:8081/login/oauth2/code/my-auth")
                .scope("READ_DATETIME").scope("WRITE_DATETIME")
                .scope(OidcScopes.OPENID)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(clientCredentialsClient, authCodeClient, postmanAuthCodeClient);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
}
