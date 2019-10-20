package com.train4game.social.web.controllers.ui;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.service.FileService;
import com.train4game.social.service.ProfileService;
import com.train4game.social.to.UserTo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private ProfileService profileService;
    private FileService fileService;

    @GetMapping
    public String profile(@AuthenticationPrincipal AuthorizedUser authUser, Model model) {
        UserTo userTo = authUser.getUserTo();
        log.info("Get profile for user {}", userTo.getId());
        return profilePage(model, userTo);
    }

    @PostMapping
    public String updateProfile(@Validated @ModelAttribute UserTo userTo, BindingResult result,
                                @RequestParam("avatar") MultipartFile avatar,
                                Model model, @AuthenticationPrincipal AuthorizedUser authUser,
                                SessionStatus status) throws IOException {
        if (result.hasErrors()) {
            return profilePage(model, userTo);
        }
        fileService.saveAvatar(avatar, authUser.getId());
        userTo.setId(authUser.getId());
        profileService.update(userTo);
        authUser.updateUserTo(userTo);
        status.setComplete();
        log.info("Update profile {}", userTo);
        return "redirect:/profile";
    }

    @GetMapping(value = "/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
    public void avatar(@AuthenticationPrincipal AuthorizedUser authUser, HttpServletResponse response) throws IOException {
        byte[] avatar = fileService.getAvatar(authUser.getId());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(avatar, response.getOutputStream());
    }

    private String profilePage(Model model, UserTo userTo) {
        model.addAttribute("userTo", userTo);
        model.addAttribute("register", true);
        model.addAttribute("captchaError", false);
        return "user";
    }
}
