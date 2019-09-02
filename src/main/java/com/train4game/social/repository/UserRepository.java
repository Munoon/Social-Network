package com.train4game.social.repository;

import com.train4game.social.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private CrudUserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User get(int id) {
        return repository.getById(id);
    }

    public boolean delete(int id) {
        return repository.deleteById(id) != 0;
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}
