package com.example.demomakebymespringsecuritywithjwt.controllers;

import com.example.demomakebymespringsecuritywithjwt.models.request.LoginRequest;
import com.example.demomakebymespringsecuritywithjwt.models.request.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthPages {

    @GetMapping("/login")
    public static String login(Model model){
        model.addAttribute("user_for_login", new LoginRequest());
        return "loginpage.html";
    }

    @GetMapping("/register")
    public static String register(Model model){
        model.addAttribute("user_for_register", new RegisterRequest());
        return "registerpage.html";
    }
}
