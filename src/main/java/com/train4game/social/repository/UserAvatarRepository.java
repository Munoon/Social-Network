package com.train4game.social.repository;

import com.train4game.social.model.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserAvatarRepository extends JpaRepository<UserAvatar, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE UserAvatar a SET a.isCurrent = false WHERE a.isCurrent = true")
    void disableAllAvatars();

    @Query("SELECT a FROM UserAvatar a WHERE a.user.id = ?1 AND a.isCurrent = true")
    UserAvatar getCurrentAvatar(int userId);
}
