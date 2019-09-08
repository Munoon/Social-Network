package com.train4game.social.web.controllers;

import com.train4game.social.model.User;
import com.train4game.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.train4game.social.web.SecurityUtil.authUserId;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = userService.get(authUserId());
        model.addAttribute("user", user);
        return "user";
    }
}
