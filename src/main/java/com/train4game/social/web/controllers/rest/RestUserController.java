package com.train4game.social.web.controllers.rest;

import com.train4game.social.model.User;
import com.train4game.social.service.UserService;
import com.train4game.social.to.UserTo;
import com.train4game.social.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestUserController.URL)
@Slf4j
public class RestUserController {
    public static final String URL = "/rest/user";

    @Autowired
    private UserService service;

    @GetMapping("/{id}")
    public UserTo get(@PathVariable int id) {
        User user = service.get(id);
        return UserUtil.asTo(user);
    }
}
