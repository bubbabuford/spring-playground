package com.example.tacocloud.security;

import com.example.tacocloud.security.filters.UsernamePasswordAuthenticationFilter;
import com.example.tacocloud.security.providers.UsernamePasswordAuthenticationProvider;
import com.example.tacocloud.security.services.SecureUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private SecureUserDetailsService secureUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new UsernamePasswordAuthenticationProvider(secureUserDetailsService, passwordEncoder()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAt(new UsernamePasswordAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/design").hasRole("USER")
                .and().authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .formLogin();
    }
}
