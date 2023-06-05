package org.example.service;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.model.User;
import org.example.storage.user.storage.FriendDao;
import org.example.storage.user.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("UserServiceDb")
@Slf4j
@Getter
public class UserServiceDb implements UserService {
    private final FriendDao friendsDao;
    private final UserStorage userStorage;

    @Autowired
    public UserServiceDb(FriendDao friendsDao, @Qualifier("dbUserStorage") UserStorage userStorage) {
        this.friendsDao = friendsDao;
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(int userId, int friendUserId) {
        friendsDao.addFriend(userId, friendUserId);
    }

    @Override
    public List<User> getFriends(int userId) {
        List<User> friendList = new ArrayList<>();
        for (Integer friendId : friendsDao.getUserAllFriendsId(userId)) {
            friendList.add(userStorage.getUserById(friendId));
        }
        return friendList;
    }

    @Override
    public void deleteFriend(int userId, int friendUserId) {
        friendsDao.deleteFriend(userId, friendUserId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendUserId) {
        checkUserAndFriendId(userId, friendUserId);
        List<Integer> mutualFriendsId = userStorage.getUserById(userId).getFriendsId().stream()
                .filter(userStorage.getUserById(friendUserId).getFriendsId()::contains)
                .collect(toList());
        List<User> mutualFriends = new ArrayList<>();
        for (int mutualId : mutualFriendsId) {
            mutualFriends.add(userStorage.getUserById(mutualId));
        }
        log.debug("User {} friend {} mutual friends list {}", userId, friendUserId, mutualFriends);
        return mutualFriends;
    }

    @Override
    public UserStorage getUserStorage() {
        return userStorage;
    }


    private void checkUserAndFriendId(int userId, int friendId) {
        if ( userId <= 0 || friendId <= 0) {
            log.debug("Check user {} check friend {}", userId, friendId);
            throw new NotFoundException(String.format("User with id:%s or user friend with id:%s not found",
                    userId, friendId));
        }
    }
}
