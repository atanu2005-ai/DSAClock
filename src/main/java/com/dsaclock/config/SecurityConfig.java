package com.dsaclock.config;

import com.dsaclock.security.CustomOAuth2AuthRequestResolver;
import com.dsaclock.security.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    //jwt filter reference
    private final JwtFilter jwtFilter;

    //client registration repository reference
    private final ClientRegistrationRepository clientRegistrationRepository;

    //auth success handler reference
    private final AuthenticationSuccessHandler oauthsuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter,
                          ClientRegistrationRepository clientRegistrationRepository,
                          AuthenticationSuccessHandler oauthsuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oauthsuccessHandler = oauthsuccessHandler;
    }


    @Bean //filter chain bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthorizationRequestRepository<OAuth2AuthorizationRequest>
                                                           oauth2AuthorizationRequestRepository) throws Exception {



        CustomOAuth2AuthRequestResolver resolver =
                new CustomOAuth2AuthRequestResolver(clientRegistrationRepository);

        ClientRegistration google =
                clientRegistrationRepository.findByRegistrationId("google");

        System.out.println("CLIENT REGISTRATION: ");
        System.out.println(google);

        return http
                .csrf(csrf -> csrf.disable()) //disabling csrf protection
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->{

            //PUBLIC ENDPOINTS
            auth.requestMatchers(HttpMethod.GET,("/api/problems/**")).permitAll();

            //OPTION GIVEN BY BROWSER
            auth.requestMatchers(HttpMethod.OPTIONS, ("/**")).permitAll();

            //AUTHENTICATION FREE REGISTRATION
            auth.requestMatchers(HttpMethod.POST,("/api/users")).permitAll();

            //AUTHENTICATION FREE  LOCAL LOGIN
            auth.requestMatchers(HttpMethod.POST,("/api/login")).permitAll();

            //GOOGLE LOGIN
            auth.requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll();

            //AUTHENTICATED ENDPOINTS
            auth.anyRequest().authenticated();
        })
                .oauth2Login(oauth ->
                        oauth.authorizationEndpoint(endpoint ->
                                endpoint.authorizationRequestResolver(resolver)
                                        .authorizationRequestRepository(
                                                oauth2AuthorizationRequestRepository))
                                .successHandler(oauthsuccessHandler))

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //goes through jwt filter first
                .build();
    }

    @Bean //password encoder bean. Returns password encoder object
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //authentication bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
