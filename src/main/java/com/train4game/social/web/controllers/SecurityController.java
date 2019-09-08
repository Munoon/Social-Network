package com.train4game.social.web.controllers;

import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

import static com.train4game.social.util.UserUtil.createNewFromTo;

@Controller
public class SecurityController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return loginPage(model, new UserTo(), false);
    }

    @GetMapping("/register")
    public String register(Model model) {
        return loginPage(model, new UserTo(), true);
    }

    @PostMapping("/register")
    public String register(@Valid UserTo userTo, BindingResult result, Model model, SessionStatus sessionStatus) {
        if (result.hasErrors())
            return loginPage(model, userTo, true);

        userService.create(createNewFromTo(userTo));
        sessionStatus.setComplete();
        return "redirect:/profile";
    }

    private String loginPage(Model model, UserTo userTo, boolean register) {
        model.addAttribute("userTo", userTo);
        model.addAttribute("register", register);
        return "login";
    }
}
