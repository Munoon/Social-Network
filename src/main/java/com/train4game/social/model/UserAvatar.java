package com.train4game.social.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_avatars")
public class UserAvatar extends AbstractBaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_current", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isCurrent = true;

    public UserAvatar() {
    }

    public UserAvatar(Integer id, User user) {
        super(id);
        this.user = user;
    }
}
