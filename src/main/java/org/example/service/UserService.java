package org.example.service;

import org.example.model.User;
import org.example.storage.user.storage.UserStorage;

import java.util.List;

public interface UserService {
    void addFriend(int userId, int friendUserId);

    List<User> getFriendList(int userId);

    void deleteFriend(int userId,int friendUserId);

    List<User> getListMutualFriends(int userId, int friendUserId);

    UserStorage getUserStorage();
}
