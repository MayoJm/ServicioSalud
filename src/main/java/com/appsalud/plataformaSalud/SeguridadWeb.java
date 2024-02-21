package com.appsalud.plataformaSalud;

import com.appsalud.plataformaSalud.servicios.UsuarioPacienteServicio;
import com.appsalud.plataformaSalud.servicios.UsuarioProfesionalServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SeguridadWeb {

    @Autowired
    public UsuarioProfesionalServicio usuarioProfesionalServicio;

    @Autowired
    public UsuarioPacienteServicio usuarioPacienteServicio;

    @Autowired

    public void configureGlobal2(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioProfesionalServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(usuarioPacienteServicio)
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            for (GrantedAuthority authority : authentication.getAuthorities()) {
                                if (authority.getAuthority().equals("ROLE_PACIENTE")) {
                                    response.sendRedirect("/paciente/dashboard-paciente");
                                    return;
                                } else if (authority.getAuthority().equals("ROLE_PROFESIONAL")) {
                                    response.sendRedirect("/profesional/dashboard-profesional");
                                    return;
                                } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                                    response.sendRedirect("/admin/dashboard-admin");
                                    return;
                                }
                            }
                            // Si no encuentra algun rol en particular redirecciona a inicio por defecto.
                            response.sendRedirect("/");
                        })
                        .permitAll())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/images/**", "/css/**", "/js/**", "/WEB-INF/views/**").permitAll()
                        .requestMatchers("/ejemplo/ejemplo").hasRole("ADMIN")
                        .requestMatchers("/ejemplo/ejemplo").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/ejemplo/ejemplo").hasAnyRole("USER")
                        .anyRequest().permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector2() {
        return new HandlerMappingIntrospector();
    }
}
