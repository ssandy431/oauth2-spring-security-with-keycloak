package com.app.oauth2.home.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	private final ClientRegistrationRepository clientRegistrationRepository;
	
	public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
		this.clientRegistrationRepository=clientRegistrationRepository;
	}
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors(CorsConfigurer::disable)
		.csrf(CsrfConfigurer::disable)
		.authorizeHttpRequests(req ->req.requestMatchers("/").permitAll().anyRequest().authenticated())
		.oauth2Login(Customizer.withDefaults())
		.logout(logout ->logout
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.logoutSuccessHandler(oidcLogoutSuccessHandler())
				)
		;
		return http.build();
	}
	 private LogoutSuccessHandler oidcLogoutSuccessHandler() {
		    OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
		            new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
		    oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
		    return oidcLogoutSuccessHandler;
		  }
}
