package com.train4game.social.web.controllers.rest;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.train4game.social.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(RestProfileController.URL)
@Slf4j
public class RestProfileController {
    public static final String URL = "/rest/profile";

    @Autowired
    private UserService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTo get(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("User {} get his profile", authUser.getId());
        return authUser.getUserTo();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated @RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("Update {} with id {}", userTo, authUser.getId());
        assureIdConsistent(userTo, authUser.getId());
        service.update(userTo);
    }
}
