package org.example.service;

import org.example.model.User;
import org.example.storage.user.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ArrayList<User> getAll() {
        return userStorage.getAllUsers();
    }

    public User add(User user) {
        return userStorage.createUser(user);
    }

    public User update(User user) {
        return userStorage.updateUser(user);
    }

    public User getUser(int userId) {
        return userStorage.getUser(userId);
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
        userStorage.deleteFriend(friendId, userId);
    }

    public void deleteUser(User user) {
        userStorage.deleteUser(user);
    }

    public ArrayList<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    public ArrayList<User> getCommonFriends(int userId, int userIdToCompare) {
        return userStorage.getCommonFriends(userId, userIdToCompare);
    }
}