package com.jobrecruitment.config;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.CompanyStatus;
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
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/company/**").hasAuthority("COMPANY")
                        .requestMatchers(HttpMethod.POST, "/company/upload-logo").hasAuthority("COMPANY")
                        .requestMatchers(HttpMethod.POST, "/company/remove-logo").hasAuthority("COMPANY")

                        .requestMatchers(HttpMethod.POST, "/recruiter/job/*/update").hasAnyAuthority("RECRUITER", "COMPANY")
                        .requestMatchers("/recruiter/job/**").hasAnyAuthority("RECRUITER", "ADMIN")
                        .requestMatchers("/recruiter/job/*/edit").hasAnyAuthority("RECRUITER", "COMPANY")

                        .requestMatchers("/recruiter/application/**").hasAnyAuthority("RECRUITER", "COMPANY")
                        .requestMatchers(HttpMethod.POST, "/recruiter/application/**").hasAnyAuthority("RECRUITER", "COMPANY")

                        .requestMatchers("/applicant/**").hasAuthority("APPLICANT")
                        .requestMatchers("/applicant/skills/**").hasAuthority("APPLICANT")

                        .requestMatchers("/admin/job/**").hasAuthority("ADMIN")


                        .requestMatchers(HttpMethod.POST, "/auth/set-role").permitAll()
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
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


                if (user.getRole() == User.Role.COMPANY) {
                    if (user.getCompany() == null || user.getCompany().getStatus() != CompanyStatus.APPROVED) {
                        response.sendRedirect("/company/create");
                        return;
                    }
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
