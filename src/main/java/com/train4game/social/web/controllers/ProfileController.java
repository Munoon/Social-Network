package com.train4game.social.web.controllers;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.service.ProfileService;
import com.train4game.social.to.PasswordForgotTo;
import com.train4game.social.to.PasswordResetTo;
import com.train4game.social.to.UserTo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private ProfileService profileService;

    @GetMapping
    public String profile(@AuthenticationPrincipal AuthorizedUser authUser, Model model) {
        return profilePage(model, authUser.getUserTo());
    }

    @PostMapping
    public String updateProfile(@Validated @ModelAttribute UserTo userTo, BindingResult result,
                                Model model, @AuthenticationPrincipal AuthorizedUser authUser,
                                SessionStatus status) {
        if (result.hasErrors()) {
            return profilePage(model, userTo);
        }
        userTo.setId(authUser.getId());
        profileService.update(userTo);
        authUser.updateUserTo(userTo);
        status.setComplete();
        return "redirect:/profile";
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
        profileService.processForgotPassword(passwordForgotTo);
        return "redirect:/login?resetPassword";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String tokenMsg, Model model) {
        profileService.getToken(tokenMsg);
        model.addAttribute("passwordResetTo", new PasswordResetTo());
        return "token/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute PasswordResetTo passwordResetTo, BindingResult result) {
        if (result.hasErrors()) {
            return "token/reset-password";
        }
        profileService.resetPassword(passwordResetTo);
        return "redirect:/login?resetted";
    }

    private String profilePage(Model model, UserTo userTo) {
        model.addAttribute("userTo", userTo);
        model.addAttribute("register", true);
        return "user";
    }
}
