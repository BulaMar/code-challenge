package org.challenge.twitterclone.repository;

import org.challenge.twitterclone.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public Optional<User> findById(String userName) {
        return Optional.ofNullable(users.get(userName));
    }

    public User save(User user) {
        return users.put(user.getUserName(), user);
    }
}
