package com.train4game.social.web.controllers;

import com.train4game.social.model.Token;
import com.train4game.social.model.User;
import com.train4game.social.service.RegistrationService;
import com.train4game.social.to.RegisterUserTo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class RegistrationController {
    private RegistrationService registrationService;

    @GetMapping("/login")
    public String login(Model model) {
        return loginPage(model, new RegisterUserTo(), false);
    }

    @GetMapping("/register")
    public String register(Model model) {
        return loginPage(model, new RegisterUserTo(), true);
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userTo") RegisterUserTo userTo, BindingResult result,
                           Model model, SessionStatus sessionStatus) {
        if (result.hasErrors()) {
            return loginPage(model, userTo, true);
        }
        User user = registrationService.getUser(userTo);
        if (user == null) {
            return registerUser(userTo, sessionStatus);
        }
        if (!user.isEnabled()) {
            Token token = registrationService.getToken(user);
            model.addAttribute("email", token.getUser().getEmail());
            return "token/email-resend";
        }
        return "redirect:/login";
    }

    @PostMapping("/resend-token")
    public String resendToken(@RequestParam String email) {
        registrationService.resendToken(email);
        return "redirect:/login?disabled";
    }

    @GetMapping("/confirm-token")
    public String enable(@RequestParam String token) {
        User user = registrationService.enableUser(token);
        return "redirect:/login?enabled&email=" + user.getEmail();
    }

    private String registerUser(RegisterUserTo userTo, SessionStatus status) {
        registrationService.registerUser(userTo);
        status.setComplete();
        return "redirect:/login?disabled";
    }

    private String loginPage(Model model, RegisterUserTo userTo, boolean register) {
        model.addAttribute("userTo", userTo);
        model.addAttribute("register", register);
        return "login";
    }
}

