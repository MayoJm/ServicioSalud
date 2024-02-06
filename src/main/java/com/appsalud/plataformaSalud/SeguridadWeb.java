package com.appsalud.plataformaSalud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SeguridadWeb {                  // Clase de configuración de seguridad Spring Security 6 (investigar Spring Security 6).

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/images/**", "/css/**", "/js/**", "/WEB-INF/views/**").permitAll()
                                .requestMatchers("/ejemplo/ejemplo").hasRole("ADMIN")
                                .requestMatchers("/ejemplo/ejemplo").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/ejemplo/ejemplo").hasAnyRole("USER")
                                .anyRequest().permitAll()
                )
                .logout(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build();
        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}password")
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector2() {
        return new HandlerMappingIntrospector();
    }
}
