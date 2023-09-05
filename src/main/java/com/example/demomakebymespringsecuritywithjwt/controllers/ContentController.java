package com.example.demomakebymespringsecuritywithjwt.controllers;

import com.example.demomakebymespringsecuritywithjwt.models.User;
import com.example.demomakebymespringsecuritywithjwt.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/content")
public class ContentController {

    //problema rezolvata de chat gpt
    //am injectat repository ul prin constructor
    private final UserRepository userRepository;

    @Autowired
    public ContentController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public String infoAccount(Model model, String email){
        if(email == null){
            model.addAttribute("msg", "Mai intai logheaza-te.");
            return AuthPages.login(model);
        }
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
