package com.example.demomakebymespringsecuritywithjwt.controllers;

import com.example.demomakebymespringsecuritywithjwt.models.User;
import com.example.demomakebymespringsecuritywithjwt.repositoy.UserRepository;
import com.example.demomakebymespringsecuritywithjwt.models.request.RegisterRequest;
import com.example.demomakebymespringsecuritywithjwt.security.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/post")
public class InputInfoProcessing {

    @Autowired
    private UserRepository userRepository;

    private JwtTokenProvider jwtTokenProvider;

    public InputInfoProcessing(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/requestInfoLogin")
    public String requestInfoLogin(
            //@ModelAttribute LoginRequest inputLog,
            @RequestParam String email,
            @RequestParam String password,
            Model model
            ,final RedirectAttributes redirectAttributes
    ) {
        System.out.println(email);
        String token = "404";
        if (userRepository.existsByEmail(email)) {
            if (userRepository.findByEmail(email).getPassword().equals(password)) {
                model.addAttribute("msg", "Conectat cu succes.");

                //generare token
                token = jwtTokenProvider.createToken(email);

                //crearea instantei autenticata si adaugarea acesteia in securitycontexholder
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);


                //return new ContentController(userRepository).infoAccount(model, token);
                model.addAttribute("token", token);
                return new ContentController(userRepository, jwtTokenProvider).infoAccount(model, token);
                //return "index.html";
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
            redirectAttributes.addFlashAttribute("msg", "Email folosit deja pentru alt utilizator.");
            return "redirect:/auth/register";
        }
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("msg", "Inregistrat cu succes!");
        return "redirect:/auth/login";
    }

}
