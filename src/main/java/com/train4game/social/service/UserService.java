package com.train4game.social.service;

import com.train4game.social.AuthorizedUser;
import com.train4game.social.model.User;
import com.train4game.social.repository.UserRepository;
import com.train4game.social.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.train4game.social.util.UserUtil.updateUserFromTo;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user) {
        Assert.notNull(user, "User must be not null");
        return repository.save(prepareToSave(user));
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public User get(int id) {
        return repository.get(id);
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    public List<User> getAll() {
        return repository.getAll();
    }

    public void update(User user) {
        Assert.notNull(user, "User must be not null");
        repository.save(prepareToSave(user));
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.getId());
        repository.save(prepareToSave(updateUserFromTo(user, userTo)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null)
            throw new UsernameNotFoundException("User with email " + email + " not found");
        return new AuthorizedUser(user);
    }

    private User prepareToSave(User user) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
