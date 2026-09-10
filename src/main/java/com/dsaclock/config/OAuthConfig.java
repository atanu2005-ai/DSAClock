package com.dsaclock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
public class OAuthConfig {

    @Bean //authorization request repository bean. Returns authorization request repository object
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest>
    oauth2AuthorizationRequestAuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository(); //makes sure that the authorization request is
                                                                      // stored in the session and can be retrieved later
    }
}
