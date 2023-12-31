package com.example.demomakebymespringsecuritywithjwt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

//    //folisit pentru a crea o semnatura unica pentru fiecare start al aplicatiei
    private final long startApp = new Date().getTime();
    private final String secretKey = "secretesgrbhbgsyerbgywrthi54t58498ki8gabi26" + startApp;
//    private final String secretKey = "secretesgrbhbgsyerbgywrthi54t58498ki8gabi26";
    private final int validityInMilliseconds =3600000; // adica o ora / 60 de minute

    @Autowired
    private InfoUserDetailsService userDetailsService;

    //generarea unei semnaturi de tip key pentru semnarea tokenului
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email){
        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey())
                .compact();

        System.out.println("JwtTokenProvider - Tokenul este pentru utilizatorul: "+ email +" este: " + token);

        return token;
    }


    //Crearea unei instante de tip Authentication care pastreaza persoana logata
    //CRED CA TREBUIA SA ADAUG ROLURI PENTRU A LE ADAUGA CA getAuthorities
    public Authentication getAuthentication(String token){
        System.out.println("JwtTokenProvider - getAuthentication");
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //parsam tokenul pentru a obtine emailul
    public String getEmail(String token) {
        // return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJwt(token).getBody().getSubject();
        String subject = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.out.println("JwtTokenProvider - Obtinerea email ului din token: " + subject);
        return subject;
    }
    //metoda care preia tokenul jwt din headerul http
    public String getToken(HttpServletRequest httpRequest){
        System.out.println("JwtTokenProvider - obtinerea tokenului din httpRequest");
        String bearerToken = httpRequest.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    //pentru verificarea daca tokenul nu este exprirat
    public boolean validateToken(String token) {
        if (token != null) {
            try {

                    long token_expTime = Jwts.parserBuilder().setSigningKey(getSigningKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getExpiration()
                            .getTime();

                    Date expTime = new Date(token_expTime);
                    System.out.println("JwtTokenProvider - validateToken expTime: " + expTime);
                    return (new Date()).before(expTime);

            } catch (JwtException | IllegalArgumentException e) {
                System.out.println("JwtTokenProvider - validateToken este null");
                throw e;
            }
        }
        return false;
    }


    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String wantThisCookie = "jwt";
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (wantThisCookie.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
