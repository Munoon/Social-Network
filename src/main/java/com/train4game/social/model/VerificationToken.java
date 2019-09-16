package com.train4game.social.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens")
@Data
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;
    @Id
    @Column(nullable = false, updatable = false)
    private String token = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_time", updatable = false, nullable = false)
    private LocalDateTime dateTime = LocalDateTime.now();

    public boolean isValid(LocalDateTime now) {
        return now.isBefore(dateTime.plusMinutes(EXPIRATION));
    }
}
