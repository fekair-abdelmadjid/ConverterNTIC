package com.example.web;

import com.example.entities.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/register")
    public String registerIndex(Model model) {
        model.addAttribute("user", new User());
        return "logup";
    }

    @PostMapping(value = "/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "logup";
        }
        if (userService.userPresent(user.getEmail())) {
            model.addAttribute("existe", true);
            return "logup";
        }
        userService.createUser(user);
        return "index";
    }
}
