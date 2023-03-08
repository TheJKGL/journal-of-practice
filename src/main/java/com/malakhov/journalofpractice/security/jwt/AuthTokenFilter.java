package com.malakhov.journalofpractice.security.jwt;

import com.malakhov.journalofpractice.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    /**
     * What we do inside doFilterInternal():
     * – get JWT from the Authorization header (by removing Bearer prefix)
     * – if the request has JWT, validate it, parse username from it
     * – from username, get UserDetails to create an Authentication object
     * – set the current UserDetails in SecurityContext using setAuthentication(authentication) method.
     * <p>
     * After this, everytime you want to get UserDetails, just use SecurityContext like this:
     * UserDetails userDetails =
     * 	(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     * 	<p>
     * // userDetails.getUsername()
     * // userDetails.getPassword()
     * // userDetails.getAuthorities()
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = jwtUtils.getTokenFromRequest(request);
        try {
            if(jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
                Authentication authentication = jwtUtils.getAuthentication(jwtToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        filterChain.doFilter(request, response);
    }
}
