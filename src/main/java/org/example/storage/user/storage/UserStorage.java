package org.example.storage.user.storage;

import org.example.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    User getUserById(int id);

    Collection<User> getAllUsers();

    User updateUser(User user);

    void deleteAllUsers();

    void deleteUser(int userId);

}
