package org.example.productcategory.config;

import lombok.RequiredArgsConstructor;
import org.example.productcategory.exception.handler.CustomAccessDeniedHandler;
import org.example.productcategory.exception.handler.CustomAuthenticationEntryPoint;
import org.example.productcategory.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/users/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
          throws Exception {
    return config.getAuthenticationManager();
  }
}