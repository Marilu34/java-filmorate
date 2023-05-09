package org.example.storage.user;

import org.example.exceptions.ValidationException;
import org.example.model.User;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static final Map<Integer, User> users = new HashMap<>();


    private static int userID = 1;

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        user.setId(generateID());
        validate(user);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() <= 0) {
            throw new ValidationException("Нельзя обновить Пользователя с айди =" + user.getId());
        }
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        }

        return user;
    }

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = users.get(userId);
        validate(user);
        user.getFriendsIds().add(friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = users.get(userId);
        user.getFriendsIds().remove(friendId);
    }

    @Override
    public ArrayList<User> getFriends(int userId) {
        User user = users.get(userId);
        Set<Integer> listFriendsIds = user.getFriendsIds();
        ArrayList<User> friendList = new ArrayList<>();

        for (Integer friendId : listFriendsIds) {
            friendList.add(users.get(friendId));
        }
        return friendList;
    }

    @Override
    public ArrayList<User> getCommonFriends(int userId, int userIdToCompare) {
        User userFirst = users.get(userId);
        User userSecond = users.get(userIdToCompare);
        Set<Integer> commonFriendsList = new HashSet<>();
        for (Integer friendsUserFirst : userFirst.getFriendsIds()) {
            for (Integer friendsUserSecond : userSecond.getFriendsIds()) {
                if (friendsUserFirst.equals(friendsUserSecond)) {
                    commonFriendsList.add(friendsUserFirst);
                }
            }
        }
        ArrayList<User> commonFriends = new ArrayList<>();

        for (Integer friendId : commonFriendsList) {
            commonFriends.add(users.get(friendId));
        }
        return commonFriends;
    }

    private int generateID() {
        return userID++;
    }


    private void validate(@Valid User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может содержать пробелы");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("почта не может быть пустой");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения должна быть не будущей");
        }
    }
}