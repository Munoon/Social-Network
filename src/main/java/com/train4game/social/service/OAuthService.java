package com.train4game.social.service;

import com.train4game.social.addons.OAuthClientResources;
import com.train4game.social.model.User;
import com.train4game.social.model.VKOAuth;
import com.train4game.social.repository.UserRepository;
import com.train4game.social.service.email.AsyncEmailSender;
import com.train4game.social.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class OAuthService {
    private UserRepository repository;
    private UserService userService;
    private AsyncEmailSender emailSender;
    private OAuthClientResources vk;
    private RestOperations restOperations;

    public User getUserFromGoogleOAuth(Map map) {
        String googleId = (String) map.get("sub");
        String email = (String) map.get("email");
        User user = repository.findByGoogleIdOrEmail(googleId, email);
        if (user == null) {
            String password = UserUtil.generatePassword();
            user = UserUtil.createUserFromGoogleMap(map, password);
            userService.create(user);
            emailSender.sendOAuthEmail(email, password, (String) map.get("given_name"), "Google");
            log.info("Create new user from Google oAuth - {}", user);
        }
        if (user.getGoogleId() == null) {
            user.setGoogleId(googleId);
            userService.update(user);
            log.info("Set to registered user Google oAuth ID - {}", user);
        }
        return user;
    }

    public User getUserFromFacebookOAuth(Map map) {
        String facebookId = (String) map.get("id");
        String email = (String) map.get("email");
        User user = repository.findByFacebookIdOrEmail(facebookId, email);
        if (user == null) {
            String password = UserUtil.generatePassword();
            user = UserUtil.createUserFromFacebookMap(map, password);
            userService.create(user);
            emailSender.sendOAuthEmail(email, password, (String) map.get("first_name"), "Facebook");
            log.info("Create new user from Facebook oAuth - {}", user);
        }
        if (user.getFacebookId() == null) {
            user.setFacebookId(facebookId);
            userService.update(user);
            log.info("Set to registered user Facebook oAuth ID - {}", user);
        }
        return user;
    }

    public void setVKOAuth(int userId, Integer vkId) {
        repository.setVkOauth(userId, vkId);
    }

    public User readVkOAuth(VKOAuth vkoAuth) {
        User user;
        // Find user
        if (vkoAuth.getEmail() == null) {
            user = repository.findByVkId(vkoAuth.getUserId());
        } else {
            user = repository.findByVkIdOrEmail(vkoAuth.getUserId(), vkoAuth.getEmail());
        }

        // Register user
        if (user == null && vkoAuth.getEmail() != null) {
            user = createUser(vkoAuth);
        } else if (user == null) {
            return null;
        }

        // Check if user have vk id
        // If not - set vk id
        if (user.getVkId() == null) {
            user.setVkId(vkoAuth.getUserId());
            userService.update(user);
        }

        return user;
    }

    private User createUser(VKOAuth vkoAuth) {
        // Getting map info
        String url = vk.getResource().getUserInfoUri() +
                "&user_ids=" + vkoAuth.getUserId() +
                "&access_token=" + vkoAuth.getAccessToken();
        Map map = restOperations.getForEntity(url, Map.class).getBody();
        Map<String, Object> userMap = ((List<Map<String, Object>>) map.get("response")).get(0);

        // Creating user with generated password
        userMap.put("email", vkoAuth.getEmail());
        String password = UserUtil.generatePassword();
        User user = UserUtil.createUserFromVkMap(userMap, password);
        userService.create(user);

        // Send password to email
        emailSender.sendOAuthEmail(vkoAuth.getEmail(), password, (String) userMap.get("first_name"), "VK");
        log.info("Create new user from VK oAuth - {}", user);
        return user;
    }
}
