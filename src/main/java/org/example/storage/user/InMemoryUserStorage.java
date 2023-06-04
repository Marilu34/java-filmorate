package org.example.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.model.User;
import org.example.storage.user.storage.UserStorage;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int id = 1;

    private int setId() {
        return id++;
    }

    @Override
    public User addUser(User user) {
        if (userMap.containsKey(user.getId())) {
            log.debug("User with id:{}, already exist", user.getId());
            throw new NotFoundException("Id already use");
        }
        user.setId(setId());
        validate(user);
        user.setFriendsId(new HashSet<>());
        userMap.put(user.getId(), user);
        log.info("User with id:{} create", user.getId());
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        if (!userMap.containsKey(userId)) {
            log.debug("User id:{}", userId);
            throw new NotFoundException("Id not found");
        }
        userMap.remove(userId);
    }

    @Override
    public User updateUser(User user) {
        if (!userMap.containsKey(user.getId())) {
            log.debug("User id:{}", user.getId());
            throw new NotFoundException("Id not found");
        }
        if (user.getFriendsId() == null) {
            user.setFriendsId(userMap.get(user.getId()).getFriendsId());
        }
        validate(user);
        userMap.put(user.getId(), user);
        log.info("User with id:{} update", user.getId());
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    @Override
    public void deleteAllUsers() {
        userMap.clear();
    }

    @Override
    public User findUserById(int id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException("User not found");
        }
        return userMap.get(id);
    }

    private void validate(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("User id:{}, with empty name", user.getId());
        }
    }
}
