package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(@Qualifier("UserServiceDb") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.getUserStorage().addUser(user);
    }


    @GetMapping
    public Collection<User> getAll() {
        return userService.getUserStorage().getAllUsers();
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.getUserStorage().updateUser(user);
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") int userId) {
        return userService.getUserStorage().findUserById(userId);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userService.getUserStorage().deleteAllUsers();
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") int userId) {
        userService.getUserStorage().deleteUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") int userId,
                          @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable("userId") int userId) {
        return userService.getFriendList(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") int userId,
                             @PathVariable("friendId") int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{friendsId}")
    public List<User> getMutualFriends(@PathVariable int userId,
                                       @PathVariable int friendsId) {
        return userService.getListMutualFriends(userId, friendsId);
    }
}