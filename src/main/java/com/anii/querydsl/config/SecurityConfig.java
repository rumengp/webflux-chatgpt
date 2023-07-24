package com.anii.querydsl.config;

import com.anii.querydsl.config.jwt.JwtTokenAuthenticationFilter;
import com.anii.querydsl.config.jwt.JwtTokenProvider;
import com.anii.querydsl.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtTokenProvider tokenProvider,
                                                         ReactiveAuthenticationManager reactiveAuthenticationManager) {
        return http.authorizeExchange(spec -> spec
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/**").authenticated()
                )
                .authenticationManager(reactiveAuthenticationManager)
                .csrf(csrfSpec -> csrfSpec.disable())
                .cors(corsSpec -> corsSpec.disable())
                .formLogin(formLoginSpec -> formLoginSpec.disable())
                .httpBasic(httpBasicSpec -> httpBasicSpec.disable())
                .logout(logoutSpec -> logoutSpec.disable())
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(u -> User
                        .withUsername(u.getUsername())
                        .password(u.getPassword())
                        .authorities(CollectionUtils.emptyIfNull(u.getRoles()).toArray(new String[0]))
                        .accountExpired(!u.getEnabled())
                        .credentialsExpired(!u.getEnabled())
                        .disabled(!u.getEnabled())
                        .accountLocked(!u.getEnabled())
                        .build()
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}