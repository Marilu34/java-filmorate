package org.example.storage.user.storage;

import java.util.List;

public interface FriendDao {
    void addFriend(int userId, int friendUserId);

    List<Integer> getFriendsIdList(int userId);

    void deleteFriend(int userId, int friendUserId);
}
