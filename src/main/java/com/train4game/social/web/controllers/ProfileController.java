package com.train4game.social.web.controllers;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.View;
import com.train4game.social.model.Recaptcha;
import com.train4game.social.service.RecaptchaService;
import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;

import static com.train4game.social.util.UserUtil.createNewFromTo;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        loginPage(model, new UserTo());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        registerPage(model, new UserTo());
        return "login";
    }

    @PostMapping("/register")
    public String register(@Validated(View.UserRegister.class) UserTo userTo, BindingResult result, Model model, SessionStatus sessionStatus) {
        if (result.hasErrors()) {
            registerPage(model, userTo);
            return "login";
        }
        userService.create(createNewFromTo(userTo));
        sessionStatus.setComplete();
        return "redirect:/profile/login?email=" + userTo.getEmail();
    }

    @GetMapping
    public String profile(@AuthenticationPrincipal AuthorizedUser authUser, Model model) {
        loginPage(model, authUser.getUserTo());
        return "user";
    }

    @PostMapping
    public String updateProfile(@Validated @ModelAttribute UserTo userTo, BindingResult result,
                                Model model, @AuthenticationPrincipal AuthorizedUser authUser,
                                SessionStatus status) {
        if (result.hasErrors()) {
            loginPage(model, userTo);
            return "user";
        }
        userTo.setId(authUser.getId());
        userService.update(userTo);
        authUser.updateUserTo(userTo);
        status.setComplete();
        return "redirect:/profile";
    }

    private void loginPage(Model model, UserTo userTo) {
        loginOrRegisterPage(model, userTo, false);
    }

    private void registerPage(Model model, UserTo userTo) {
        loginOrRegisterPage(model, userTo, true);
    }

    private void loginOrRegisterPage(Model model, UserTo userTo, boolean register) {
        model.addAttribute("userTo", userTo);
        model.addAttribute("register", register);
        model.addAttribute("captchaError", false);
    }
}
