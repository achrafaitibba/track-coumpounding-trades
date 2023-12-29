package com.achrafaitibba.trackcompoundingtrades.configuration.authenticationConfiguration;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurationFilter {

    private final JwtAuthenticationFIlter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
                /** to allow cors from all origins */
                .csrf()
                .disable()
                .cors()
                /////////////////
                .and()
                /** authorized endpoints: doesn't require authentication */
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/account/register",
                        "/api/v1/account/authenticate",
                        "/api/v1/account/refresh-token",
                        "/api/v1/enums/timeframes",
                        "api/v1/enums/TradeSortingOptions"
                )
                .permitAll()
                //////////////////////
                /** ask authentication for any other request */
                .anyRequest()
                .authenticated()
                .and()
                ////////////////////////////
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /** enable frames, I used it for swagger documentation to put it inside an IFRAME tag */
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(
                        (request,
                         response,
                         authentication) ->
                        SecurityContextHolder.clearContext())
        ;
        return httpSecurity.build();
    }
}
