package org.example.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.example.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        return userService.add(user);
    }


    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAll();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int id) {

        return userService.getUser(id);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") User user) {
        log.info("User with id=" + user.getId() + " has been deleted");
        userService.deleteUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void createFriend(@PathVariable("userId") int userId,
                          @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendList(@PathVariable("userId") int userId) {
        return userService.getFriends(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") int userId,
                             @PathVariable("friendId") int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{friendsId}")
    public List<User> getCommonFriendList(@PathVariable int userId,
                                       @PathVariable int friendsId) {
        return userService.getCommonFriends(userId, friendsId);
    }
}