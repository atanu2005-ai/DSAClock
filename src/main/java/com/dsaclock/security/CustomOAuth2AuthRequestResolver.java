package com.dsaclock.security;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;


//request resolver to preserve auth action (login or register) in the authorization request
public class CustomOAuth2AuthRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAuth2AuthRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository,
                "/oauth2/authorization");
    }


    @Override
    public @Nullable OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);

        String action = request.getParameter("action"); //fetching action parameter from request

        if(action != null && authorizationRequest != null) { //if action parameter is present and authorization request is not null

            request.getSession().setAttribute("auth_action", action);
            return
                    OAuth2AuthorizationRequest.from(authorizationRequest)
                            .attributes(params -> params.put("action", action))
                            .build(); //attaching action parameter to the authorization request attributes
        }

        return authorizationRequest;
    }

    @Override
    public @Nullable OAuth2AuthorizationRequest resolve(HttpServletRequest request,
                                                        String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);

        String action = request.getParameter("action"); //fetching action parameter from request

        if(action != null && authorizationRequest != null) { //if action parameter is present and authorization request is not null

            request.getSession().setAttribute("auth_action", action);
            return
                    OAuth2AuthorizationRequest.from(authorizationRequest)
                            .attributes(params -> params.put("action", action))
                            .build(); //attaching action parameter to the authorization request attributes
        }

        return authorizationRequest;
    }
}
