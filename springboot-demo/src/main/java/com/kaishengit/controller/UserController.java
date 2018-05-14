package com.kaishengit.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public String home(Model model) {

        List<String> name = Arrays.asList();
        model.addAttribute("message","<em>你好,SpringBoot!</em>");
        model.addAttribute("age",23);
        model.addAttribute("names",name);
        model.addAttribute("id",1001);
        model.addAttribute("money",123123123123123L);


        return "user/home";
    }

}
