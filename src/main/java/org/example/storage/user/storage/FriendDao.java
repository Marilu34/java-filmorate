package org.example.storage.user.storage;

import java.util.List;

public interface FriendDao {
    List<Integer> getFriends(int userId);

    void addFriend(int userId, int friendUserId);

    List<Integer> getUserAllFriendsId(int userId);
    void deleteFriend(int userId, int friendUserId);
}
