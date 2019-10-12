package com.train4game.social.web.controllers.ui;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.service.ProfileService;
import com.train4game.social.to.UserTo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private ProfileService profileService;

    @GetMapping
    public String profile(@AuthenticationPrincipal AuthorizedUser authUser, Model model) {
        UserTo userTo = authUser.getUserTo();
        log.info("Get profile for user {}", userTo.getId());
        return profilePage(model, userTo);
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
        log.info("Update profile {}", userTo);
        return "redirect:/profile";
    }

    private String profilePage(Model model, UserTo userTo) {
        model.addAttribute("userTo", userTo);
        model.addAttribute("register", true);
        model.addAttribute("captchaError", false);
        return "user";
    }
}
