package org.example.service;

import org.example.model.User;
import org.example.storage.user.storage.UserStorage;

import java.util.List;

public interface UserService {
    void addFriend(long userId, long friendUserId);

    List<User> getFriendList(long userId);

    void deleteFriend(long userId, long friendUserId);

    List<User> getListMutualFriends(long userId, long friendUserId);

    UserStorage getUserStorage();
}
