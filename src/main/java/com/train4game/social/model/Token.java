package com.train4game.social.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "tokens")
public class Token {
    public static final int EXPIRATION = 60 * 24;
    public static final int RESEND_NUMBER = 1;
    public enum Type {REGISTER, RESET_PASSWORD}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token", nullable = false, updatable = false)
    private String token = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private Type type;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDateTime expirationDate = creationDate.plusMinutes(EXPIRATION);

    @Column(name = "resend_number", nullable = false)
    private int resendNumber = 0;

    public Token(Type type, User user) {
        this.type = type;
        this.user = user;
    }
}
