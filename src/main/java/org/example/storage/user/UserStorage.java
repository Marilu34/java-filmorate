package org.example.storage.user;

import org.example.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUser(int userId);



    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    User deleteUser(User user);

    List<User> getFriendsList(int userId);

    List<User> getCommonFriends(int userId, int userIdToCompare);
}