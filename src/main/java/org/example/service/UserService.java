package org.example.service;

import org.example.model.User;
import org.example.storage.user.storage.UserStorage;

import java.util.List;

public interface UserService {
    UserStorage getUserStorage(); //для вызова других методов

    void addFriend(int userId, int friendUserId);

    List<User> getFriends(int userId);

    void deleteFriend(int userId, int friendUserId);

    List<User> getCommonFriends(int userId, int friendUserId);

}
