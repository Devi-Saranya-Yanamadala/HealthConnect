package com.cts.healthconnect.audit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. Validate the Header format
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // 3. Extract username (subject) from token
                String username = jwtUtils.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    
                    // 4. Extract the 'role' claim using the JwtUtils helper
                    // Your Auth service uses .claim("role", role)
                    String role = jwtUtils.extractClaim(token, claims -> String.valueOf(claims.get("role")));

                    if (role != null && !role.equalsIgnoreCase("null")) {
                        
                        // 5. Apply ROLE_ prefix (Required for Spring Security hasRole() / hasAnyRole())
                        String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

                        // Debug print to verify the fix in your IDE Console
                        System.out.println(">>> AUDIT SECURITY: Authorized " + username + " with " + prefixedRole);

                        var authorities = Collections.singletonList(new SimpleGrantedAuthority(prefixedRole));

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                username, null, authorities);

                        // 6. Set the user in the Security Context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // This will trigger if the secret key in Audit doesn't match Auth
                System.err.println(">>> AUDIT SECURITY ERROR: " + e.getMessage());
            }
        }

        // 7. Proceed to the next filter or the Controller
        filterChain.doFilter(request, response);
    }
}