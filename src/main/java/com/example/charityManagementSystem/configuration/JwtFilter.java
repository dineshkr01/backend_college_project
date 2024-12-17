package com.example.charityManagementSystem.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.charityManagementSystem.services.JWTService;
import com.example.charityManagementSystem.services.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/public/")) {
            System.out.println("Redirecting the public api");
            filterChain.doFilter(request, response);
            return;
        }

        String concatenatedUsername = null;
        String body = null;
        char role = '\0';

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader == null) {
            System.out.println("extracting Request Body: " + body);
            System.out.println("extracting Role extracted from body: " + role);
            body = extractRequestBody(request);
            role = extractRoleFromJson(body);
        }

        else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
            concatenatedUsername = username;
            System.out.println("Username from token is : " + username);
        }

        if ((role == '1' || role == '2' || role == '3') && username != null) {
            concatenatedUsername = role + username;
            System.out.println("Concatenated Username: " + concatenatedUsername);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(MyUserDetailService.class)
                    .loadUserByUsername(concatenatedUsername);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }

    private char extractRoleFromJson(String body) {
        try {
            // Use ObjectMapper to parse the JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(body);
            JsonNode roleNode = jsonNode.get("role"); // Get the "Role" field from JSON

            if (roleNode != null) {
                return roleNode.asText().charAt(0); // Extract the first character (the role)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return '\0'; // Return null character if no role is found or parsing fails
    }
}
