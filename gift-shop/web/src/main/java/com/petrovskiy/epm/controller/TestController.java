package com.petrovskiy.epm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class TestController {

    @RequestMapping("/login")
    public String authenticate() {
        return "login";
    }
}
