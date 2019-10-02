package com.train4game.social.service;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.model.User;
import com.train4game.social.repository.UserRepository;
import com.train4game.social.to.UserSettingsTo;
import com.train4game.social.util.UserUtil;
import com.train4game.social.util.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.train4game.social.util.UserUtil.prepareToSave;
import static com.train4game.social.util.exception.Messages.NOT_FOUND;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository repository;
    private PasswordEncoder encoder;

    public User create(User user) {
        Assert.notNull(user, "User must be not null");
        return repository.save(prepareToSave(user, encoder));
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public User get(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "user")));
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email).orElseThrow(() ->
                new NotFoundException(String.format(NOT_FOUND, "user")));
    }

    public User getUserFromGoogleOAuth(Map map) {
        String googleId = (String) map.get("sub");
        String email = (String) map.get("email");
        User user = getByGoogleIdOrEmail(googleId, email);
        if (user == null) {
            user = UserUtil.createUserFromGoogleMap(map);
            create(user);
        }
        if (user.getGoogleId() == null) {
            user.setGoogleId(googleId);
            update(user);
        }
        return user;
    }

    public User getUserFromFacebookOAuth(Map map) {
        String facebookId = (String) map.get("id");
        String email = (String) map.get("email");
        User user = getByFacebookIdOrEmail(facebookId, email);
        if (user == null) {
            user = UserUtil.createUserFromFacebookMap(map);
            create(user);
        }
        if (user.getFacebookId() == null) {
            user.setFacebookId(facebookId);
            update(user);
        }
        return user;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public void update(User user) {
        Assert.notNull(user, "User must be not null");
        repository.save(prepareToSave(user, encoder));
    }

    public void updateLocale(int id, String locale) {
        repository.updateLocale(id, locale);
    }

    public void enable(User user) {
        repository.enable(user);
    }

    public UserSettingsTo getUserSettings(int id) {
        return UserUtil.asSettings(get(id));
    }

    @Transactional
    public void updateSettings(int userId, UserSettingsTo settingsTo) {
        User user = get(userId);
        user.setLocale(settingsTo.getLocale());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email.toLowerCase());
        if (user == null)
            throw new UsernameNotFoundException("User with email " + email + " not found");
        return new AuthorizedUser(user);
    }

    private User getByGoogleIdOrEmail(String googleId, String email) {
        return repository.findByGoogleIdOrEmail(googleId, email).orElse(null);
    }

    private User getByFacebookIdOrEmail(String facebookId, String email) {
        return repository.findByFacebookIdOrEmail(facebookId, email).orElse(null);
    }
}
