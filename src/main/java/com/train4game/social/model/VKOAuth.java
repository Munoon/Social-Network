package com.train4game.social.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VKOAuth {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("email")
    private String email;
}
