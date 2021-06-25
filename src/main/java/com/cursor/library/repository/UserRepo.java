package com.cursor.library.repository;

import com.cursor.library.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepo {
    private final Map<String, User> userMap = new HashMap<>();

    public Optional<User> findByUsername(String username) {
        return Optional.of(userMap.get(username));
    }

    public void save(User user) {
        userMap.put(user.getUsername(), user);
    }

}