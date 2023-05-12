package org.example.storage.user;

import org.example.model.User;

import java.util.ArrayList;

public interface UserStorage {

    ArrayList<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUser(int userId);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    void deleteUser(User user);

    ArrayList<User> getFriends(int userId);

    ArrayList<User> getCommonFriends(int userId, int userIdToCompare);
}