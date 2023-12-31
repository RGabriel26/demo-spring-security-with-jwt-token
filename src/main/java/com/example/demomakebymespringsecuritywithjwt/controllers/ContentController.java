package com.example.demomakebymespringsecuritywithjwt.controllers;

import com.example.demomakebymespringsecuritywithjwt.models.User;
import com.example.demomakebymespringsecuritywithjwt.repositoy.UserRepository;
import com.example.demomakebymespringsecuritywithjwt.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ContentController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/user")
//    public String infoAccount(Model model, String token){
    public String infoAccount(Model model,
                              HttpServletRequest req){

        System.out.println("ContentController - infoAccount");
        System.out.println("ContentController - http requestul:" + req.getRequestURL());

        //obtinere email din token
        String email = jwtTokenProvider.getEmail(jwtTokenProvider.getTokenFromCookie(req));

        User user = new User(userRepository.findByEmail(email).getFirstname(),
                userRepository.findByEmail(email).getLastname(),
                userRepository.findByEmail(email).getEmail(),
                userRepository.findByEmail(email).getPassword());

        List<User> users = new LinkedList<>();
        users.add(user);
        model.addAttribute("users", users);
        return "contentpage.html";
    }
}

