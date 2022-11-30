package com.petrovskiy.epm.controller;


import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String signUp(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }
        userService.create(userDto);
        return "redirect:/api/products";
    }

    @GetMapping("login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "signup";
    }

//    todo
/*    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "signup";
    }*/
}