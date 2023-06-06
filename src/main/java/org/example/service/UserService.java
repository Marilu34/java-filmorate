package org.example.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.model.User;
import org.example.storage.user.storage.FriendDao;
import org.example.storage.user.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@Getter
public class UserService {
    private final FriendDao friendsDao;
    private final UserStorage userStorage;

    @Autowired
    public UserService(FriendDao friendsDao, UserStorage userStorage) {
        this.friendsDao = friendsDao;
        this.userStorage = userStorage;
    }

    private void check(int userId, int friendId) {
        if (userId <= 0 || friendId <= 0) {
            log.debug("Проверка Пользователя {} Проверка друга Пользователя {}", userId, friendId);
            throw new NotFoundException(String.format("Пользователя с id:%s или друга Пользователя с id:%s не обнаружено",
                    userId, friendId));
        }
    }

    public void addFriend(int userId, int friendUserId) {
        friendsDao.addFriend(userId, friendUserId);
    }


    public List<User> getFriends(int userId) {
        List<User> friendList = new ArrayList<>();
        for (Integer friendId : friendsDao.getFriendsIdList(userId)) {
            friendList.add(userStorage.getUserById(friendId));
        }
        return friendList;
    }


    public void deleteFriend(int userId, int friendUserId) {
        friendsDao.deleteFriend(userId, friendUserId);
    }


    public List<User> getCommonFriends(int userId, int friendUserId) {
        check(userId, friendUserId);
        List<Integer> commonFriendIds = userStorage.getUserById(userId).getFriendsId().stream()
                .filter(userStorage.getUserById(friendUserId).getFriendsId()::contains)
                .collect(toList());
        List<User> commonFriends = new ArrayList<>();
        for (int commonIds : commonFriendIds) {
            commonFriends.add(userStorage.getUserById(commonIds));
        }
        log.debug("Пользователь {} с Пользователем {} имеет общих друзей {}", userId, friendUserId, commonFriends);
        return commonFriends;
    }

    public UserStorage getUserStorage() { //чтобы вызвать методы UserStorage
        return userStorage;
    }
}
