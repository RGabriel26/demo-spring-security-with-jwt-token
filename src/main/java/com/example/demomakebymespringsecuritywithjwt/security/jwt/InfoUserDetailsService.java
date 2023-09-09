package com.example.demomakebymespringsecuritywithjwt.security.jwt;

import com.example.demomakebymespringsecuritywithjwt.models.User;
import com.example.demomakebymespringsecuritywithjwt.repositoy.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InfoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public InfoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //cautam in database contul dupa email ul dat
        User appUser = userRepository.findByEmail(email);

        if(appUser == null){
            throw new UsernameNotFoundException("InfoUserDetailsService - Nu am putut gasi un cont caruia sa ii corespunda email ul dat: " + email);
        }

        return org.springframework.security.core.userdetails.User//
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();


    }
}
