package org.example.service;



import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.UserFriendException;
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
        if (userStorage.findUserById(userId).getFriendsId().contains(userStorage.findUserById(friendUserId).getId())) {
            log.debug("User {} add friend user {} , user friend list {}", userId, friendUserId,
                    userStorage.findUserById(userId).getFriendsId());
            throw new UserFriendException(String.format(
                    "User %s already friends with %s", userStorage.findUserById(userId).getName(),
                    userStorage.findUserById(friendUserId).getName()));
        }
        if (userId == friendUserId) {
            log.debug("user {} friend {}", userId, friendUserId);
            throw new UserFriendException("You cannot be friend for you");
        }
        log.debug("User {} add friend user {}", userId, friendUserId);
        userStorage.findUserById(userId).getFriendsId().add(friendUserId);
        userStorage.findUserById(friendUserId).getFriendsId().add(userId);
        friendsDao.addFriend(userId,friendUserId);
    }

    @Override
    public List<User> getFriendList(int  userId) {
        if (userId <= 0) {
            log.debug("User {}", userId);
            throw new NotFoundException(String.format("user with id:%s not found", userId));
        }
        List<User> friendList = new ArrayList<>();
        for (int friendId : userStorage.findUserById(userId).getFriendsId()) {
            friendList.add(userStorage.findUserById(friendId));
        }
        log.debug("User {} users friend list {}", userId, friendList);
        return friendList;
    }

    @Override
    public void deleteFriend(int userId, int friendUserId) {
        checkUserAndFriendId(userId, friendUserId);
        if (!userStorage.findUserById(userId).getFriendsId().contains(userStorage.findUserById(friendUserId).getId())) {
            log.debug("User {} deleted friend {} from friend list {}", userId, friendUserId,
                    userStorage.findUserById(userId).getFriendsId());
            throw new UserFriendException(String.format(
                    "You not friends with %s", userStorage.findUserById(friendUserId).getName()
            ));
        }
        if (userId == friendUserId) {
            log.debug("User {} deleted friend {}", userId, friendUserId);
            throw new UserFriendException("You cannot delete yourself from friends");
        }
        log.debug("User {} delete friend {}", userId, friendUserId);
        userStorage.findUserById(userId).getFriendsId().remove(friendUserId);
        userStorage.findUserById(friendUserId).getFriendsId().remove(userId);
    }

    @Override
    public List<User> getListMutualFriends(int userId, int friendUserId) {
        checkUserAndFriendId(userId, friendUserId);
        List<Integer> mutualFriendsId = userStorage.findUserById(userId).getFriendsId().stream()
                .filter(userStorage.findUserById(friendUserId).getFriendsId()::contains)
                .collect(toList());
        List<User> mutualFriends = new ArrayList<>();
        for (int mutualId : mutualFriendsId) {
            mutualFriends.add(userStorage.findUserById(mutualId));
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
