package com.example.fluxoSpringOauth.config;

import com.example.fluxoSpringOauth.repository.OauthRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
public class CustomRegisteredClientRepositoryConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository(OauthRepository repository) {
        return new RegisteredClientRepository() {
            @Override
            public void save(RegisteredClient registeredClient) {

            }

            @Override
            public RegisteredClient findById(String id) {
                return null;
            }

            @Override
            public RegisteredClient findByClientId(String clientId) {
                return repository.findByClientId(clientId)
                        .map(client -> RegisteredClient.withId(UUID.randomUUID().toString())
                                .clientId(client.getClientId())
                                .clientSecret(client.getClientSecret())
                                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                                .scope("read")
                                .tokenSettings(TokenSettings.builder()
                                        .accessTokenTimeToLive(Duration.ofHours(2))
                                        .build())
                                .clientSettings(ClientSettings.builder().build())
                                .build()
                        ).orElse(null);
            }
        };
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8080")
                .tokenEndpoint("/oauth/token")
                .build();
    }

    @Component
    public class CustomTokenClaims implements OAuth2TokenCustomizer<JwtEncodingContext> {

        @Override
        public void customize(JwtEncodingContext context) {
            if (context.getTokenType().getValue().equals("access_token")) {
                context.getClaims().claim("aud", "cade-rest-api");

                if (context.getPrincipal() != null) {
                    String clientId = context.getRegisteredClient().getClientId();
                    context.getClaims().claim("user_name", clientId);
                    context.getClaims().claim("authorities", List.of("INTERNAL"));
                }

                context.getClaims().claim("client_id", context.getRegisteredClient().getClientId());
            }
        }
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

