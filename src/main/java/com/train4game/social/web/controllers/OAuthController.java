package com.train4game.social.web.controllers;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.addons.OAuthClientResources;
import com.train4game.social.model.User;
import com.train4game.social.model.VKOAuth;
import com.train4game.social.service.OAuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
@AllArgsConstructor
public class OAuthController {
    private static final String VK_LOGIN_URI = "/response/vk";
    private static final String VK_CONNECT_URI = "/response/connect/vk";
    private OAuthClientResources vk;
    private RestOperations restOperations;
    private OAuthService oAuthService;

    private String vkOAuth(String url) {
        AuthorizationCodeResourceDetails client = vk.getClient();
        return "redirect:" +
                client.getUserAuthorizationUri() +
                "?client_id=" + client.getClientId() +
                "&redirect_uri=" + getClientUri(url) +
                "&scope=email" +
                "&display=page";
    }

    @GetMapping(VK_LOGIN_URI)
    public String vkLoginResponse(@RequestParam String code) {
        VKOAuth vkoAuth = readData(code, VK_LOGIN_URI);
        User user = oAuthService.readVkOAuth(vkoAuth);
        if (user == null) {
            return "redirect:/login?oauthError=true";
        } else {
            getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new AuthorizedUser(user), null, user.getRoles()));
            return "redirect:/profile";
        }
    }

    @GetMapping(VK_CONNECT_URI)
    public String vkConnectResponse(@RequestParam String code, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        VKOAuth vkoAuth = readData(code, VK_CONNECT_URI);
        oAuthService.setVKOAuth(authorizedUser.getId(), vkoAuth.getUserId());
        authorizedUser.getUserTo().setVkId(vkoAuth.getUserId());
        return "redirect:/settings";
    }

    private VKOAuth readData(String code, String redirectUrl) {
        AuthorizationCodeResourceDetails client = vk.getClient();
        String url = client.getAccessTokenUri() +
                "?client_id=" + client.getClientId() +
                "&client_secret=" + client.getClientSecret() +
                "&redirect_uri=" + getClientUri(redirectUrl) +
                "&code=" + code;
        return restOperations.getForObject(url, VKOAuth.class);
    }

    @GetMapping("/login/vk")
    public String loginVk() {
        return vkOAuth(VK_LOGIN_URI);
    }

    @GetMapping("/connect/vk")
    public String connectVk() {
        return vkOAuth(VK_CONNECT_URI);
    }

    private String getClientUri(String uri) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .build().toUriString() + uri;
    }
}
