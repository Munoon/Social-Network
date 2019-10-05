package com.train4game.social.service;

import com.train4game.social.MessageUtil;
import com.train4game.social.model.User;
import com.train4game.social.repository.UserRepository;
import com.train4game.social.service.email.Email;
import com.train4game.social.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class OAuthService {
    private UserRepository repository;
    private UserService userService;
    private EmailService emailService;
    private MessageUtil messageUtil;

    public User getUserFromGoogleOAuth(Map map) {
        String googleId = (String) map.get("sub");
        String email = (String) map.get("email");
        User user = repository.findByGoogleIdOrEmail(googleId, email);
        String password = null;
        if (user == null) {
            password = UserUtil.generatePassword();
            user = UserUtil.createUserFromGoogleMap(map, password);
            userService.create(user);
            log.info("Create new user from Google oAuth - {}", user);
        }
        if (user.getGoogleId() == null) {
            user.setGoogleId(googleId);
            userService.update(user);
            log.info("Set to registered user Google oAuth ID - {}", user);
        }
        if (password != null) {
            sendEmailWithPassword(email, password, (String) map.get("given_name"), "Google");
        }
        return user;
    }

    public User getUserFromFacebookOAuth(Map map) {
        String facebookId = (String) map.get("id");
        String email = (String) map.get("email");
        User user = repository.findByFacebookIdOrEmail(facebookId, email);
        String password = null;
        if (user == null) {
            password = UserUtil.generatePassword();
            user = UserUtil.createUserFromFacebookMap(map, password);
            userService.create(user);
            log.info("Create new user from Facebook oAuth - {}", user);
        }
        if (user.getFacebookId() == null) {
            user.setFacebookId(facebookId);
            userService.update(user);
            log.info("Set to registered user Facebook oAuth ID - {}", user);
        }
        if (password != null) {
            sendEmailWithPassword(email, password, (String) map.get("first_name"), "Facebook");
        }
        return user;
    }

    public User getUserFromVKOAuth(Map map) {
        Map<String, Object> userMap = ((List<Map<String, Object>>) map.get("response")).get(0);
        Integer vkId = (Integer) userMap.get("id");
        User user = repository.findByVkId(vkId);
        if (user == null) {
//            generatePasswordAndSendEmail(email);
            user = UserUtil.createUserFromVkMap(userMap);
            userService.create(user);
            log.info("Create new user from VK oAuth - {}", user);
        }
        return user;
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmailWithPassword(String email, String password, String name, String oauth) {
        String title = messageUtil.getMessage("email.oauth.password.title");

        HashMap<String, Object> model = new HashMap<>();
        model.put("title", title);
        model.put("text", messageUtil.getMessage("email.oauth.password.text", name, oauth));
        model.put("password", messageUtil.getMessage("email.oauth.password.password", password));
        model.put("subtext", messageUtil.getMessage("email.oauth.password.subtext", email));

        Email newEmail = new Email("SocialNetwork", email, title, "email/oauth-password", model);

        try {
            emailService.sendEmail(newEmail);
        } catch (MessagingException e) {
            log.error("Can't send email for user with email {}", email, e);
        }
    }
}
