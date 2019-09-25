package com.train4game.social.repository;

import com.train4game.social.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    void deleteById(@Param("id") int id);

    @Query("FROM User u WHERE u.email = ?1")
    Optional<User> getByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.enabled = TRUE WHERE u = ?1")
    void enable(User user);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.locale = ?2 WHERE u.id = ?1")
    void updateLocale(int id, String locale);
}
