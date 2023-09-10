package com.example.demomakebymespringsecuritywithjwt.security.config;

import com.example.demomakebymespringsecuritywithjwt.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

//
//    @Bean
//    @Order(1)
//    public SecurityFilterChain allAccesChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception{
//        http
//                .securityMatchers((matchers) -> matchers
//                        .requestMatchers(
//                                mvc.pattern(HttpMethod.POST,"/post/**"),
//                                mvc.pattern(HttpMethod.GET,"/auth/**"),
//                                mvc.pattern("/h2/**")
//                        )
//
//                )
//
//                .httpBasic(withDefaults());
//
//        return http.build();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(request -> request
////                        .anyRequest().authenticated()
//
//                          .anyRequest().authenticated()
//
//                )
////                .formLogin(Customizer.withDefaults())
//                .apply(new JwtTokenFilterConfigurer(jwtTokenProvider))
//
//        ;
//        http.httpBasic(withDefaults());
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(mvcMatcherBuilder.pattern("/post/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/auth/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/content/**")).authenticated()
                                .anyRequest().authenticated()
                )
//                .formLogin((fromLogin) ->  fromLogin
//                                .loginPage("/auth/login")
//
//                )

                .apply(new JwtTokenFilterConfigurer(jwtTokenProvider))
        ;
        return http.build();
    }

//    @Bean
//    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
//        return new MvcRequestMatcher.Builder(introspector);
//    }



//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }


}
