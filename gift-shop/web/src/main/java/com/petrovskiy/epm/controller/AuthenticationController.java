package com.petrovskiy.epm.controller;


import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.dto.AuthenticationRequestDto;
import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.security.CookiesAuthenticationFilter;
import com.petrovskiy.epm.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(UserService userService,JwtTokenProvider jwtTokenProvider
            , AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

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
    public String showLoginForm(Model model) {
        model.addAttribute("requestDto",new AuthenticationRequestDto());
        return "login";
    }

    @GetMapping("signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user",new UserDto());
        return "signup";
    }

    @PostMapping("/login")
    public String authenticate(@ModelAttribute("requestDto") @Valid AuthenticationRequestDto requestDto,
                               BindingResult result,
                               HttpServletResponse response) {
        if (result.hasErrors()) {
            return "login";
        }
        Object name = requestDto.getName();
        Object password = requestDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, password));

        UserDto userDto = userService.findByName(String.valueOf(name));
        String token = jwtTokenProvider.createToken(userDto.getFirstName(), userDto.getRole().getName());
        System.out.println(token);
        HttpHeaders responseHeaders = new HttpHeaders();
        Cookie cookie = new Cookie(CookiesAuthenticationFilter.COOKIE_NAME,token);
        cookie.setPath("/");
        response.addCookie(cookie);
        addAccessTokenCookie(responseHeaders, token);
        return "redirect:/api/products";
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, String token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, token);
    }


    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "signup";
    }


    /*@PostMapping("/logOut")
    public void signOut(HttpServletRequest request){
        SecurityContextHolder.clearContext();
        Optional<Cookie> authCookie = Stream.of(Optional.
                ofNullable(request.getCookies()).orElse(new javax.servlet.http.Cookie[0]))
                .filter(cookie -> CookiesAuthenticationFilter.COOKIE_NAME
                        .equals(cookie.getName()))
                .findFirst();

        authCookie.ifPresent(cookie -> cookie.setMaxAge(0));
    }*/
}