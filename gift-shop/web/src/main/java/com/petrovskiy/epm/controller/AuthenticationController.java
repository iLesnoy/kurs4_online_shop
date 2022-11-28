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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;


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
    public UserDto signUp(@ModelAttribute("user")  UserDto userDto, HttpHeaders request) {
        return userService.create(userDto);
    }


    @PostMapping("/login")
    public String authenticate(@ModelAttribute(name="requestDto") AuthenticationRequestDto requestDto,
                               HttpServletResponse response, Model model) {
        Object name = requestDto.getName();
        Object password = requestDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, password));

        UserDto userDto = userService.findByName(String.valueOf(name));
        String token = jwtTokenProvider.createToken(userDto.getFirstName(), userDto.getRole().getName());
        HttpHeaders responseHeaders = new HttpHeaders();
        Cookie cookie = new Cookie(CookiesAuthenticationFilter.COOKIE_NAME,token);
        cookie.setPath("/");
        response.addCookie(cookie);
        addAccessTokenCookie(responseHeaders, token);
        return "login";
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, String token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, token);
    }

    @GetMapping("registration")
    public String showRegistrationForm() {
        return "registration";
    }

    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user")
                                              UserDto registrationDto) {

        userService.create(registrationDto);
        return "redirect:/registration?success";
    }

    @PostMapping("/logOut")
    public void signOut(HttpServletRequest request){
        SecurityContextHolder.clearContext();
        Optional<Cookie> authCookie = Stream.of(Optional.
                ofNullable(request.getCookies()).orElse(new javax.servlet.http.Cookie[0]))
                .filter(cookie -> CookiesAuthenticationFilter.COOKIE_NAME
                        .equals(cookie.getName()))
                .findFirst();

        authCookie.ifPresent(cookie -> cookie.setMaxAge(0));
    }
}