
package com.example.onlineBusBookingdemo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
               .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/login","/register","/index").permitAll()
                        .requestMatchers(HttpMethod.POST,"/login","/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"buses/admin/**","feedback/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"buses/admin/**","feedback/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/buses/search","/bus/{busId}/{userId}/select","/feedback/form/{userId}","/profile/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/buses/search","/confirm-booking","user/update","change-password").hasRole("USER")

                        .anyRequest().authenticated());



        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
