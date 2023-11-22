package com.learn.google.authenticator.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String[] WHITELIST_PATH = {"/**", "/public/**"};

	@Bean
	@Primary
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
		return new InMemoryUserDetailsManager(new User(USERNAME, passwordEncoder.encode(PASSWORD), new ArrayList<>()));
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity, InMemoryUserDetailsManager inMemoryUserDetailsManager, PasswordEncoder passwordEncoder) throws Exception {
		final AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(inMemoryUserDetailsManager).passwordEncoder(passwordEncoder);
		final AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		return httpSecurity
				.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.requestMatchers(WHITELIST_PATH).permitAll().requestMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationManager(authenticationManager)
				.httpBasic(Customizer.withDefaults())
				.exceptionHandling(m -> m.accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")))
				.build();
	}


}


