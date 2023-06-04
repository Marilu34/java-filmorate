package org.example.service;



import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.FriendException;
import org.example.model.User;
import org.example.storage.user.storage.FriendsDao;
import org.example.storage.user.storage.UserStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("inMemoryUserService")
@Getter
@Slf4j
public class UserServiceInMemory implements UserService {
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    public UserServiceInMemory(@Qualifier("dbUserStorage") UserStorage userStorage, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    @Override
    public void addFriend(int userId, int friendUserId) {
        checkUserAndFriendId(userId, friendUserId);
        if (userStorage.getUserById(userId).getFriendsId().contains(userStorage.getUserById(friendUserId).getId())) {
            log.debug("User {} add friend user {} , user friend list {}", userId, friendUserId,
                    userStorage.getUserById(userId).getFriendsId());
            throw new FriendException(String.format(
                    "User %s already friends with %s", userStorage.getUserById(userId).getName(),
                    userStorage.getUserById(friendUserId).getName()));
        }
        if (userId == friendUserId) {
            log.debug("user {} friend {}", userId, friendUserId);
            throw new FriendException("You cannot be friend for you");
        }
        log.debug("User {} add friend user {}", userId, friendUserId);
        userStorage.getUserById(userId).getFriendsId().add(friendUserId);
        userStorage.getUserById(friendUserId).getFriendsId().add(userId);
        friendsDao.addFriend(userId,friendUserId);
    }

    @Override
    public List<User> getFriends(int  userId) {
        if (userId <= 0) {
            log.debug("User {}", userId);
            throw new NotFoundException(String.format("user with id:%s not found", userId));
        }
        List<User> friendList = new ArrayList<>();
        for (int friendId : userStorage.getUserById(userId).getFriendsId()) {
            friendList.add(userStorage.getUserById(friendId));
        }
        log.debug("User {} users friend list {}", userId, friendList);
        return friendList;
    }

    @Override
    public void deleteFriend(int userId, int friendUserId) {
        checkUserAndFriendId(userId, friendUserId);
        if (!userStorage.getUserById(userId).getFriendsId().contains(userStorage.getUserById(friendUserId).getId())) {
            log.debug("User {} deleted friend {} from friend list {}", userId, friendUserId,
                    userStorage.getUserById(userId).getFriendsId());
            throw new FriendException(String.format(
                    "You not friends with %s", userStorage.getUserById(friendUserId).getName()
            ));
        }
        if (userId == friendUserId) {
            log.debug("User {} deleted friend {}", userId, friendUserId);
            throw new FriendException("You cannot delete yourself from friends");
        }
        log.debug("User {} delete friend {}", userId, friendUserId);
        userStorage.getUserById(userId).getFriendsId().remove(friendUserId);
        userStorage.getUserById(friendUserId).getFriendsId().remove(userId);
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

    private void checkUserAndFriendId(int userId, int friendId) {
        if ( userId <= 0 || friendId <= 0) {
            log.debug("Check user {} check friend {}", userId, friendId);
            throw new NotFoundException(String.format("User with id:%s or user friend with id:%s not found",
                    userId, friendId));
        }
    }
}
