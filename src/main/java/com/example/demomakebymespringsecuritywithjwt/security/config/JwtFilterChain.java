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

        //caz cand tokenul este obtinut din cookies
        String token = jwtTokenProvider.getTokenFromCookie(request);


        if(jwtTokenProvider.validateToken(token)) {
            System.out.println("JwtFilterChain - email ul obtinut din token: " + jwtTokenProvider.getEmail(token));
            System.out.println("JwtFilterChain - token valid: " + jwtTokenProvider.validateToken(token));


            //daca tokenul este expirat, redirect pentru logare din nou
            try {
                if (token != null && jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
                    System.out.println("JwtFilterChain - ContexHolder actualizat cu succes");
                }
            } catch (CustomException ex) {
                //this is very important, since it guarantees the user is not authenticated at all
                SecurityContextHolder.clearContext();
                response.sendError(ex.getHttpStatus().value(), ex.getMessage());
                return;
            }
        }else{
            System.out.println("JwtFilterChain - tokenul este null sau expirat.");
        }

        filterChain.doFilter(request, response);
    }

}
