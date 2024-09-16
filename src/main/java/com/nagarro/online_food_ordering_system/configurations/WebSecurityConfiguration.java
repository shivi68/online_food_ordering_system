package com.nagarro.online_food_ordering_system.configurations;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nagarro.online_food_ordering_system.enums.UserRole;
//import com.nagarro.online_food_ordering_system.services.auth.jwt.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {
	
	@Autowired
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	
	   @Autowired
	    private final UserDetailsService userDetailsService;
	
	  public WebSecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
	        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	        this.userDetailsService = userDetailsService;
	   }
	

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf(AbstractHttpConfigurer::disable)
	            .authorizeHttpRequests(request -> request
	                .requestMatchers("/api/auth/**").permitAll()
	                .requestMatchers("/api/admin/allCuisine").permitAll() 
	                .requestMatchers("/api/admin/cuisine/{title}").permitAll() 
	                .requestMatchers("/api/admin/**").hasAuthority(UserRole.ADMIN.name())
	                .requestMatchers("api/customer/userId").permitAll()
	                .requestMatchers("/api/order/**").authenticated() 
	                .requestMatchers("/api/order/admin/**").hasAuthority(UserRole.ADMIN.name())  
	                .requestMatchers("/api/order/customer/**").hasAuthority(UserRole.CUSTOMER.name())  
	                .requestMatchers("/api/dishes/{dishesId}").permitAll()
	                .requestMatchers("/api/dishes/{dishId}/allReviews").permitAll()
	                .requestMatchers("/api/dishes/{dishId}/allRatings").permitAll()
	                .requestMatchers("/api/dishes/{categoryId}/allDishes").permitAll()
	                .requestMatchers("/api/dishes/{categoryId}/dishes/{title}").permitAll()
	                .requestMatchers("api/dishes/sortedByRating").permitAll()
	                .requestMatchers("/api/dishes/sortedByOrderCount").permitAll()
	                .requestMatchers("/api/dishes/**").authenticated() 
	                .anyRequest().authenticated())
	            .exceptionHandling(exceptions -> exceptions
	            		.authenticationEntryPoint(customAuthenticationEntryPoint())
	                    .accessDeniedHandler(new CustomAccessDeniedHandler()))
	            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authenticationProvider(authenticationProvider())
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	        return http.build();
	    }
	
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
	
	@Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
	
	public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Authentication token is required\"}");
        }
    }

    public class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
           // response.getWriter().write("{\"error\": \"Only admin is authorized to add categories\"}");
            response.getWriter().write("{\"error\": \"Access Denied You are not authorized for this.\"}");
        }
    }
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
}
