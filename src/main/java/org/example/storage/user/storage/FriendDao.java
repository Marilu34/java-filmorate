package org.example.storage.user.storage;

import java.util.List;

public interface FriendDao {
    void addFriend(int userId, int friendUserId);

    List<Integer> getFriends(int userId);


    List<Integer> getUserAllFriendsId(int userId);

    void deleteFriend(int userId, int friendUserId);
}
