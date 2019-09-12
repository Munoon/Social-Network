package com.train4game.social.model;

import com.fasterxml.jackson.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recaptcha {
    private final boolean success;
    private final String challengeTs;
    private final String hostname;
    private final ErrorCode[] errorCodes;

    @JsonCreator
    public Recaptcha(
            @JsonProperty("success") boolean success,
            @JsonProperty("challenge_ts") String challengeTs,
            @JsonProperty("hostname") String hostname,
            @JsonProperty("error-codes") ErrorCode[] errorCodes) {
        this.success = success;
        this.challengeTs = challengeTs;
        this.hostname = hostname;
        this.errorCodes = errorCodes;
    }

    enum ErrorCode {
        MissingSecret, InvalidSecret, MissingResponse, InvalidResponse;

        private static Map<String, ErrorCode> errorsMap = new HashMap<>();

        static {
            errorsMap.put("missing-input-secret",   MissingSecret);
            errorsMap.put("invalid-input-secret",   InvalidSecret);
            errorsMap.put("missing-input-response", MissingResponse);
            errorsMap.put("invalid-input-response", InvalidResponse);
        }

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public ErrorCode[] getErrorCodes() {
        return errorCodes;
    }

    @Override
    public String toString() {
        return "Recaptcha{" +
                "success=" + success +
                ", challengeTs='" + challengeTs + '\'' +
                ", hostname='" + hostname + '\'' +
                ", errorCodes=" + Arrays.toString(errorCodes) +
                '}';
    }
}
