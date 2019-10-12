package com.train4game.social.web.controllers;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestUserController.URL)
public class RestUserController {
    public static final String URL = "/rest/user";
    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private UserService service;

    @GetMapping
    public UserTo get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Get user");
        return authorizedUser.getUserTo();
    }
}
