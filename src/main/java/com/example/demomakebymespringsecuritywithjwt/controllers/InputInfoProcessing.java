package com.example.demomakebymespringsecuritywithjwt.controllers;

import com.example.demomakebymespringsecuritywithjwt.models.User;
import com.example.demomakebymespringsecuritywithjwt.repositoy.UserRepository;
import com.example.demomakebymespringsecuritywithjwt.models.request.RegisterRequest;
import com.example.demomakebymespringsecuritywithjwt.security.jwt.JwtTokenProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
            final RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) {
            if (userRepository.existsByEmail(email)) {
                if (userRepository.findByEmail(email).getPassword().equals(password)) {

                    //generare token
                    String token = jwtTokenProvider.createToken(email);

                    //salvarea tokenului in cookies
                    Cookie cookie = new Cookie("jwt", token);
                    cookie.setPath("/content/user");
                    cookie.setMaxAge(60 * 60); //1 ora
                    cookie.setHttpOnly(true);

                    response.addCookie(cookie);
                    System.out.println("InputInfoProcessing - cookies salvate.");

                    return "redirect:/content/user";
                }
            }
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

    @PostMapping("/logout")
    public String logout(HttpServletResponse response,
                         final RedirectAttributes redirectAttributes){

                Cookie cookieToDelete = new Cookie("jwt", null);
                cookieToDelete.setPath("/content/user");
                cookieToDelete.setMaxAge(0);
                cookieToDelete.setHttpOnly(true);
                response.addCookie(cookieToDelete);

        System.out.println("InputInfoProcessing - logout");
        redirectAttributes.addFlashAttribute("msg", "Delogat cu succes!");
        return "redirect:/auth/login";
    }
}

//sa updatez toate cookieurile de tip jwt dar sa sterg doar de pe content user