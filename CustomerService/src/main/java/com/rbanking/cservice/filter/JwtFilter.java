package com.rbanking.cservice.filter;


import com.rbanking.cservice.service.JwtService;
import com.rbanking.cservice.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    
	Logger logger = LoggerFactory.getLogger(JwtFilter.class);


    @Autowired
    private ApplicationContext applicationContext;

    private UserDetailService getUserService() {
        return applicationContext.getBean(UserDetailService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extracting token from the request header
        
//    	logger.info("Filter doFilterInternal "+request.toString()+" -- "+response.toString());
    	
    	String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extracting the token from the Authorization header
            token = authHeader.substring(7);
            // Extracting username from the token
            userName = jwtService.extractUserName(token);
        }

        // If username is extracted and there is no authentication in the current SecurityContext
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Loading UserDetails by username extracted from the token
        	logger.info("Filter UserService loadUserByUsername");

            UserDetails userDetails = getUserService().loadUserByEmail(userName);

            // Validating the token with loaded UserDetails
            if (jwtService.validateToken(token, userDetails)) {
                // Creating an authentication token using UserDetails
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Setting authentication details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Setting the authentication token in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
            	logger.info("Filter token validateToken");

            }
        }

        // Proceeding with the filter chain
        filterChain.doFilter(request, response);
    }
}
