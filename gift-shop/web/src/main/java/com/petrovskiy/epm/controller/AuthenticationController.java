package com.petrovskiy.epm.controller;


import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String signUp(@ModelAttribute("user") UserDto userDto, HttpServletResponse response) throws IOException {
        userService.create(userDto);
//        response.sendRedirect("/auth/login");
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/signup")
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