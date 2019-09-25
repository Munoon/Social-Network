package com.train4game.social.web.controllers;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.service.ProfileService;
import com.train4game.social.to.UserTo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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

    private String profilePage(Model model, UserTo userTo) {
        model.addAttribute("userTo", userTo);
        model.addAttribute("register", true);
        model.addAttribute("captchaError", false);
        return "user";
    }
}
