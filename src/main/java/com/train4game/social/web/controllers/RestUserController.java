package com.train4game.social.web.controllers;

import com.train4game.social.model.User;
import com.train4game.social.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(RestUserController.URL)
public class RestUserController {
    public static final String URL = "/rest/user";
    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> getAll() {
        log.info("Get all users");
        return service.getAll();
    }
}
