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
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
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
        CookieClearingLogoutHandler cookies = new CookieClearingLogoutHandler("jwt");
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(mvcMatcherBuilder.pattern("/post/**")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/auth/**")).permitAll()
                                .anyRequest().authenticated())
                .formLogin((login) ->
                        login.loginPage("/auth/login")
//                                .loginProcessingUrl("/post/requestInfoLogin")
                                .defaultSuccessUrl("/content/user")
                                .permitAll())
//                .logout((logout) ->
//                        logout
//                                .deleteCookies("JSESSIONID")
//                                .deleteCookies("jwt")
//                                .logoutUrl("/post/logout")
//                                .clearAuthentication(true)
//                                .permitAll())
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


//        http
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests.requestMatchers("/post/**").permitAll()
//                                .requestMatchers("/auth/**").permitAll()
//                                .anyRequest().authenticated())
//                .formLogin((login) ->
//                        login.loginPage("/auth/login")
//                                .loginProcessingUrl("/post/requestInfoLogin")
//                                .defaultSuccessUrl("/content/user",true)
//                                .permitAll())
//                .logout((logout) ->
//                        logout.deleteCookies("JSESSIONID")
//                                .invalidateHttpSession(true)
//                                .clearAuthentication(true)
//                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                                .logoutSuccessUrl("/auth/login?logout"))
//
//                ;
//        return http.build();


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
