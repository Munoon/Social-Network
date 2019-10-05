package com.train4game.social.service;

import com.train4game.social.model.User;
import com.train4game.social.repository.UserRepository;
import com.train4game.social.util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class OAuthService {
    private UserRepository repository;
    private UserService userService;

    public User getUserFromGoogleOAuth(Map map) {
        String googleId = (String) map.get("sub");
        String email = (String) map.get("email");
        User user = repository.findByGoogleIdOrEmail(googleId, email);
        if (user == null) {
            user = UserUtil.createUserFromGoogleMap(map);
            userService.create(user);
        }
        if (user.getGoogleId() == null) {
            user.setGoogleId(googleId);
            userService.update(user);
        }
        return user;
    }

    public User getUserFromFacebookOAuth(Map map) {
        String facebookId = (String) map.get("id");
        String email = (String) map.get("email");
        User user = repository.findByFacebookIdOrEmail(facebookId, email);
        if (user == null) {
            user = UserUtil.createUserFromFacebookMap(map);
            userService.create(user);
        }
        if (user.getFacebookId() == null) {
            user.setFacebookId(facebookId);
            userService.update(user);
        }
        return user;
    }

    public User getUserFromVKOAuth(Map map) {
        Map<String, Object> userMap = ((List<Map<String, Object>>) map.get("response")).get(0);
        Integer vkId = (Integer) userMap.get("id");
        User user = repository.findByVkId(vkId);
        if (user == null) {
            user = UserUtil.createUserFromVkMap(userMap);
            userService.create(user);
        }
        return user;
    }
}
