package com.example.demomakebymespringsecuritywithjwt.controllers;

import com.example.demomakebymespringsecuritywithjwt.Service.UserService;
import com.example.demomakebymespringsecuritywithjwt.models.User;
import com.example.demomakebymespringsecuritywithjwt.repositoy.UserRepository;
import com.example.demomakebymespringsecuritywithjwt.models.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/post")
public class InputInfoProcessing {

    @Autowired
    UserRepository userRepository;

//    private final UserService userService;
//
//    public InputInfoProcessing(UserService userService) {
//        this.userService = userService;
//    }


    @PostMapping("/requestInfoLogin")
//    public String requestInfoLogin(@ModelAttribute LoginRequest userRequest, Model model,
//                                   final RedirectAttributes redirectAttributes){
//        if(userRepository.existsByEmail(userRequest.getEmail())){
//            if(userRepository.findByEmail(userRequest.getEmail()).getPassword().equals(userRequest.getPassword())){
//                model.addAttribute("msg", "Conectat cu succes.");
//                System.out.println(userRequest.getEmail());
//                return new ContentController(userRepository).infoAccount(model, userRequest.getEmail());
//            }
//        }
////        model.addAttribute("msg", "Nu s-a putut conecta.");
////        return AuthPages.login(model);
//        redirectAttributes.addFlashAttribute("msg", "Nu s-a putut conecta.");
//        return "redirect:/auth/login";
//    }
    public String requestInfoLogin( @RequestParam String email,
                                    @RequestParam String password,
                                    Model model,
                                   final RedirectAttributes redirectAttributes){
        if(userRepository.existsByEmail(email)){
            if(userRepository.findByEmail(email).getPassword().equals(password)){
                model.addAttribute("msg", "Conectat cu succes.");
                System.out.println(email);
                return new ContentController(userRepository).infoAccount(model, email);
            }
        }
//        model.addAttribute("msg", "Nu s-a putut conecta.");
//        return AuthPages.login(model);
        redirectAttributes.addFlashAttribute("msg", "Nu s-a putut conecta.");
        return "redirect:/auth/login";
    }

    @PostMapping("/requestInfoRegister")
    public String requestInfoRegister(@ModelAttribute RegisterRequest userRequest,
                                      final RedirectAttributes redirectAttributes){
        User user = new User(userRequest.getFirstname(),
                userRequest.getLastname(),
                userRequest.getEmail(),
                userRequest.getPassword());
        if(userRepository.existsByEmail(userRequest.getEmail())){
//            model.addAttribute("msg", "Email folosit deja pentru alt utilizator.");
//            return AuthPages.register(model);
            redirectAttributes.addFlashAttribute("msg", "Email folosit deja pentru alt utilizator.");
            return "redirect:/auth/register";
        }
        userRepository.save(user);
//        model.addAttribute("msg", "Inregistrat cu succes!");
//        return AuthPages.login(model);
        redirectAttributes.addFlashAttribute("msg", "Email folosit deja pentru alt utilizator.");
        return "redirect:/auth/login";
    }

}
