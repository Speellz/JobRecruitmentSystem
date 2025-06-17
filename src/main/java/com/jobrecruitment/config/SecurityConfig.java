package com.jobrecruitment.config;

import com.jobrecruitment.model.User;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.service.common.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.Authentication;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    @Lazy
    @Autowired
    private UserService userService;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/auth/set-role"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/company/**")).hasAuthority("COMPANY")
                        .requestMatchers(new AntPathRequestMatcher("/recruiter/job/*/edit", "GET")).hasAnyAuthority("RECRUITER", "COMPANY")
                        .requestMatchers(new AntPathRequestMatcher("/recruiter/job/*/update", "POST")).hasAnyAuthority("RECRUITER", "COMPANY")

                        .requestMatchers(HttpMethod.POST, "/recruiter/application/*/approve").hasAuthority("RECRUITER")
                        .requestMatchers(HttpMethod.POST, "/recruiter/application/*/reject").hasAuthority("RECRUITER")
                        .requestMatchers(HttpMethod.POST, "/recruiter/application/*/remove").hasAuthority("RECRUITER")

                        .requestMatchers("/skills/**").hasAuthority("APPLICANT")


                        .requestMatchers(new AntPathRequestMatcher("/recruiter/**")).hasAuthority("RECRUITER")
                        .requestMatchers(new AntPathRequestMatcher("/applicant/**")).hasAuthority("APPLICANT")
                        .requestMatchers(HttpMethod.POST, "/auth/set-role").permitAll()
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("loggedUser", user);
                if (user.getCompany() != null) {
                    session.setAttribute("userCompany", user.getCompany());
                }
            }

            response.sendRedirect("/");
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            String role = user.getRole().name();

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(role)
                    .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
