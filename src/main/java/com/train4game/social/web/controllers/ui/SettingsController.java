package com.train4game.social.web.controllers.ui;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.service.UserService;
import com.train4game.social.to.UserSettingsTo;
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
@RequestMapping("/settings")
@AllArgsConstructor
public class SettingsController {
    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);
    private UserService service;

    @GetMapping
    public String get(@AuthenticationPrincipal AuthorizedUser authUser, Model model) {
        model.addAttribute("settings", service.getUserSettings(authUser.getId()));
        model.addAttribute("vkConnected", authUser.getUserTo().getVkId() != null);
        return "settings";
    }

    @PostMapping
    public String post(@Validated @ModelAttribute UserSettingsTo settingsTo,
                       BindingResult result, Model model, SessionStatus status,
                       @AuthenticationPrincipal AuthorizedUser authUser) {
        if (result.hasErrors()) {
            return get(authUser, model);
        }
        int userId = authUser.getId();
        service.updateSettings(userId, settingsTo);
        authUser.getUserTo().setLocale(settingsTo.getLocale());
        status.setComplete();
        log.info("Update settings for user {} - {}", userId, settingsTo);
        return "redirect:/settings";
    }
}
