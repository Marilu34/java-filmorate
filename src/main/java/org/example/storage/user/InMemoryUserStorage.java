package org.example.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.AlreadyExistException;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.User;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDate;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private static final Map<Integer, User> users = new HashMap<>();


    private static int userID = 1;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь с id = {} уже существует", user.getId());
            throw new NotFoundException("Id уже существует");
        }
        validate(user);
        user.setId(generateID());
        user.setFriendsIds(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь с id = {} создан", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            log.debug("Пользователь с id = {}", user.getId());
            throw new NotFoundException("Id не существует");
        }
        if (user.getFriendsIds() == null) {
            user.setFriendsIds(users.get(user.getId()).getFriendsIds());
        }
        if (user.getId() <= 0) {
            throw new ValidationException("Нельзя обновить Пользователя с айди =" + user.getId());
        }
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        }
        log.info("Пользователь с id = {} обновлен", user.getId());
        return user;
    }

    @Override
    public User getUser(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь не существует");
        }
        log.info("Пользователь с id = {} найден", userId);
        return users.get(userId);
    }

    @Override
    public User deleteUser(User user) {
        if (!users.containsValue(user)) {
            log.debug("Пользователь с id = {}", user.getId());
            throw new NotFoundException("Id не существует");
        }
        users.remove(user);
        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userId <= 0 || friendId <= 0) {
            log.debug("Проверка Пользователя с id={} или Пользователь с id={}", userId, friendId);
            throw new NotFoundException(String.format("Пользователь с id:%s или Пользователь с id:%s не обнаружен",
                    userId, friendId));
        }
        if (getUser(userId).getFriendsIds().contains(getUser(friendId).getId())) {
            log.debug("Пользователь с id = {} добавил в друзья Пользователя с id={} , user friend list {}", userId, friendId,
                    getUser(userId).getFriendsIds());
            throw new AlreadyExistException(
                    "Пользователь с id = {}" + getUser(userId).getName() + "уже добавил в друзья Пользователя с id={}" +
                            getUser(friendId).getName());
        }
        if (userId == friendId) {
            log.debug("Пользователь с id = {} добавил в друзья Пользователя с id={}", userId, friendId);
            throw new ValidationException("Вы не можете себя добавить в друзья");
        }
        log.debug("Пользователь с id = {} добавил в друзья Пользователя с id={}", userId, friendId);
        getUser(userId).getFriendsIds().add(friendId);
        getUser(friendId).getFriendsIds().add(userId);
        addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (userId <= 0 || friendId <= 0) {
            log.debug("Проверка Пользователя с id={} или Пользователь с id={}", userId, friendId);
            throw new NotFoundException(String.format("Пользователь с id:%s или Пользователь с id:%s не обнаружен",
                    userId, friendId));
        }
        if (!getUser(userId).getFriendsIds().contains(getUser(friendId).getId())) {
            log.debug("Пользователь с id = {} удалил из друзей Пользователя с id={}", userId, friendId,
                    getUser(userId).getFriendsIds());
            throw new NotFoundException(String.format(
                    "Пользователь не дружил с Пользователем с id = {}", getUser(friendId).getName()
            ));
        }
        if (userId == friendId) {
            log.debug("Пользователь с id = {} удалил из друзей Пользователя с id={}", userId, friendId);
            throw new ValidationException("Вы не можете себя удалить из друзей");
        }
        log.debug("Пользователь с id = {} удалил из друзей Пользователя с id={}", userId, friendId);
        getUser(userId).getFriendsIds().remove(friendId);
        getUser(friendId).getFriendsIds().remove(userId);
    }

    @Override
    public ArrayList<User> getFriendsList(int userId) {
        if (userId <= 0) {
            log.debug("Пользователь с id = {}", userId);
            throw new NotFoundException("Id не существует");
        }
        ArrayList<User> friendList = new ArrayList<>();
        for (int friendId : getUser(userId).getFriendsIds()) {
            friendList.add(getUser(friendId));
        }
        log.debug("Пользователь с id = {} нахоится в друзьях Пользователя с id={}", userId, friendList);
        return friendList;
    }

    @Override
    public ArrayList<User> getCommonFriends(int userId, int userIdToCompare) {
        if (userId <= 0 || userIdToCompare <= 0) {
            log.debug("Проверка Пользователя с id={} или Пользователь с id={}", userId, userIdToCompare);
            throw new NotFoundException(String.format("Пользователь с id:%s или Пользователь с id:%s не обнаружен",
                    userId, userIdToCompare));
        }
        List<Integer> commonFriendsId = getUser(userId).getFriendsIds().stream()
                .filter(getUser(userIdToCompare).getFriendsIds()::contains)
                .collect(toList());
        ArrayList<User> commonFriends = new ArrayList<>();
        for (int commonIds : commonFriendsId) {
            commonFriends.add(getUser(commonIds));
        }
        log.info("Пользователь с id = {} имеет общих друзей с Пользователем с id={}", userId, userIdToCompare, commonFriends);
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