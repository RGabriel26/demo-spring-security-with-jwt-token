package com.example.demomakebymespringsecuritywithjwt.security.config;

import com.example.demomakebymespringsecuritywithjwt.customException.CustomException;
import com.example.demomakebymespringsecuritywithjwt.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilterChain extends OncePerRequestFilter {

    //trebuie creata un jwt provider
    //adaugat aici prin constructor
    private JwtTokenProvider jwtTokenProvider;

    public JwtFilterChain(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("\nJwtFilterChain");
        String token = jwtTokenProvider.getToken(request);
        System.out.println("JwtFilterChain - tokenul obtinut: " + token);
        if(token == null) {
            System.out.println(" ");
             }else{
            System.out.println("JwtFilterChain - email ul obtinut din token: " + jwtTokenProvider.getEmail(token));
            System.out.println("JwtFilterChain - token valid: " + jwtTokenProvider.validateToken(token));

        }
        System.out.println("JwtFilterChain");
        try{
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("JwtFilterChain - ContexHolder actualizat cu succes");
            }
        } catch (CustomException ex) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext();
            response.sendError(ex.getHttpStatus().value(), ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

}
