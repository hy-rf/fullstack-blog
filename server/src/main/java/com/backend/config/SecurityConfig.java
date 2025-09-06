package com.backend.config;

import com.backend.security.AuthorizationFilter;
import com.backend.security.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthorizationFilter authorizationFilter;

  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  public SecurityConfig(
    @Lazy AuthorizationFilter authorizationFilter,
    JwtAuthenticationProvider jwtAuthenticationProvider
  ) {
    this.authorizationFilter = authorizationFilter;
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http)
    throws Exception {
    return http
      .getSharedObject(AuthenticationManagerBuilder.class)
      .authenticationProvider(jwtAuthenticationProvider)
      .build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .formLogin(form -> form.disable())
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .addFilterBefore(
        authorizationFilter,
        UsernamePasswordAuthenticationFilter.class
      );
    return http.build();
  }

  @Bean
  @Profile("dev")
  public CorsFilter corsFilterDev() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("http://localhost:3000");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
