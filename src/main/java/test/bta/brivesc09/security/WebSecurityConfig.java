package test.bta.brivesc09.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.*;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private UserDetailsServiceImpl userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().authorizeRequests()
       .antMatchers("/api/v1/user/all").hasAuthority("ADMIN")
       .antMatchers("/api/v1/user/create").hasAuthority("ADMIN")
        .anyRequest().permitAll().and()
        .addFilter(new AuthenticationFilter(authenticationManager(), getApplicationContext()))
        .addFilter(new AuthorizationFilter(authenticationManager()))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    corsConfiguration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://brives-staging.herokuapp.com" ,
            "https://brives-staging.herokuapp.com",
            "http://dev.bta8jakarta.com",
            "https://dev.bta8jakarta.com"));
    corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
    corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
    corsConfiguration.addAllowedMethod(HttpMethod.PUT);
    corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
    corsConfiguration.addAllowedMethod(HttpMethod.HEAD);
    source.registerCorsConfiguration("/**", corsConfiguration);

    return source;
  }
}