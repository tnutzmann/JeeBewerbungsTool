package de.jinba.server.config;

import de.jinba.server.entity.enumuration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class is used to configure the security of the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * Configures the password encoder.
     *
     * @return a PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain.
     *
     * @param http The HttpSecurity object to configure.
     * @return A SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                // Disable CSRF protection for now
                .csrf().disable()
                // Enable form login with default success url and custom login page
                .formLogin()
                .defaultSuccessUrl("/dashboard", true)
                .loginPage("/login")
                .and()
                // Enable logout with custom logout success url
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("SESSION")
                .permitAll()
                .and()
                // Enable session management. Sessions are created if required.
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                // Configure protected paths
                .authorizeHttpRequests()
                .requestMatchers("/register", "/login").permitAll()
                .requestMatchers("/dashboard").authenticated()
                .requestMatchers("/profile/edit").authenticated()
                .requestMatchers("/profile/**", "/company/**", "/profile").authenticated()
                .requestMatchers("/offer/create", "/offer/edit").hasRole(Role.COMPANY_USER.role())
                .requestMatchers("/offer/**").authenticated()
                .requestMatchers("/application/**").authenticated()
                .requestMatchers("/**").permitAll();
        return http.build();
    }

    /**
     * Configures the authentication manager.
     *
     * @param auth The AuthenticationConfiguration object.
     * @return An AuthenticationManager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
}
