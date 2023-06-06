package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Пользователь " + user + " был создан");
        return userService.getUserStorage().createUser(user);
    }


    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getUserStorage().getAllUsers();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Пользователь " + user + " был обновлен");
        return userService.getUserStorage().updateUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") int userId) {
        log.info("Пользователь " + userId + " был получен");
        return userService.getUserStorage().getUserById(userId);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userService.getUserStorage().deleteAllUsers();
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") int userId) {
        log.info("Пользователь " + userId + " был удален");
        userService.getUserStorage().deleteUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") int userId,
                          @PathVariable("friendId") int friendId) {
        log.info("Пользователь " + userId + " добавил " + friendId + " в друзья");
        userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getAllFriends(@PathVariable("userId") int userId) {
        return userService.getFriends(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") int userId,
                             @PathVariable("friendId") int friendId) {
        log.info("Пользователь " + userId + " удалил " + friendId + " из друзей");
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{friendsId}")
    public List<User> getCommonFriends(@PathVariable int userId,
                                       @PathVariable int friendsId) {
        log.info("Пользователь " + userId + " получил список общих друзей с Пользователем " + friendsId);
        return userService.getCommonFriends(userId, friendsId);
    }
}