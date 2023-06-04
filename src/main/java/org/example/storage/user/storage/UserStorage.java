package org.example.storage.user.storage;

import org.example.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    void deleteUser(int userId);

    User updateUser(User user);

    Collection<User> getAllUsers();

    void deleteAllUsers();

    User getUserById(int id);
}
