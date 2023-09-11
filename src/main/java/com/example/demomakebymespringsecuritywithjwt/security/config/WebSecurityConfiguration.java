package com.example.demomakebymespringsecuritywithjwt.security.config;

import com.example.demomakebymespringsecuritywithjwt.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(mvcMatcherBuilder.pattern("/post/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/auth/**")).permitAll()
                                .anyRequest().authenticated())
                .formLogin((login) ->
                        login.loginPage("/auth/login")
                                .defaultSuccessUrl("/content/user")
                                .permitAll())

//                .logout((logout) ->logout
//                                .logoutUrl("/post/logout")
//                                .addLogoutHandler((request, response, authentication) -> {
//                                    for(Cookie cookie : request.getCookies()) {
//                                        String cookieName = cookie.getName();
//                                        Cookie cookieToDelete = new Cookie(cookieName, null);
//                                        cookieToDelete.setMaxAge(0);
//                                        cookieToDelete.setPath("/content/user");
//                                        response.addCookie(cookieToDelete);
//                                        System.out.println("WebSecurityConfiguration - cookiuri sterse cu succes.");
//                                    }
//                                })
//                                .clearAuthentication(true)
//                )

                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedPage("/exception/forbidden"))
                .apply(new JwtTokenFilterConfigurer(jwtTokenProvider))
        ;
        return http.build();
    }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer(){
            return (web) ->
                    web.ignoring()
                            .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
