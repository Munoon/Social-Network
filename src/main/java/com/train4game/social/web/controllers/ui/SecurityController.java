package com.train4game.social.web.controllers.ui;

import com.train4game.social.model.Token;
import com.train4game.social.model.User;
import com.train4game.social.service.ProfileService;
import com.train4game.social.service.RegistrationService;
import com.train4game.social.to.PasswordForgotTo;
import com.train4game.social.to.PasswordResetTo;
import com.train4game.social.to.RegisterUserTo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SecurityController {
    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);
    private RegistrationService registrationService;
    private ProfileService profileService;

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
            log.info("Register user {}", userTo);
            return registerUser(userTo, sessionStatus);
        }
        if (!user.isEnabled()) {
            log.info("Email resend page");
            Token token = registrationService.getToken(user);
            model.addAttribute("email", token.getUser().getEmail());
            return "token/email-resend";
        }
        return "redirect:/login";
    }

    @PostMapping("/resend-token")
    public String resendToken(@RequestParam String email) {
        log.info("Resend token for email {}", email);
        registrationService.resendToken(email);
        return "redirect:/login?disabled";
    }

    @GetMapping("/confirm-token")
    public String enable(@RequestParam String token) {
        log.info("Confirm token {}", token);
        User user = registrationService.enableUser(token);
        return "redirect:/login?enabled&email=" + user.getEmail();
    }


    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("passwordForgotTo", new PasswordForgotTo());
        return "token/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @ModelAttribute PasswordForgotTo passwordForgotTo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "token/forgot-password";
        }
        log.info("Forgot password for email {}", passwordForgotTo.getEmail());
        profileService.processForgotPassword(passwordForgotTo);
        return "redirect:/login?resetPassword";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String tokenMsg, Model model) {
        log.info("Reset password page for token {}", tokenMsg);
        profileService.getToken(tokenMsg);
        model.addAttribute("passwordResetTo", new PasswordResetTo());
        return "token/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute PasswordResetTo passwordResetTo, BindingResult result) {
        if (result.hasErrors()) {
            return "token/reset-password";
        }
        log.info("Reset password for token {}", passwordResetTo.getToken());
        profileService.resetPassword(passwordResetTo);
        return "redirect:/login?resetted";
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

