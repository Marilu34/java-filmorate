package org.example.storage.user.storage;

import java.util.List;

public interface FriendsDao {
    List<Integer> getFriends(int userId);

    void addFriend(int userId, int friendUserId);

    void deleteFriend(int userId, int friendUserId);

    List<Integer> getUserAllFriendsId(int userId);
}
