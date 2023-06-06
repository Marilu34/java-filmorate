package org.example.storage.user.Db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.User;
import org.example.storage.user.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Slf4j
@Component("dbUserStorage")
@Getter
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendsDbDao friendsDao;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendsDbDao friendsDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsDao = friendsDao;
    }

    @Override
    public User createUser(User user) {
        validate(user);
        user.setId(makeUserInDb(user));
        log.info("Пользователь " + user + " был создан");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (noExists(user.getId())) {
            log.debug("Проверка Пользователя c id = {}", user.getId());
            throw new NotFoundException("Пользователя с id:%s " + user.getId() + " не обнаружено");
        }
        String sql = "update USERS set " +
                "USER_NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        log.info("Пользователь " + user + " был обновлен");
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * from USERS";
        return jdbcTemplate.query(sql, this::makeUserDb);
    }

    @Override
    public User getUserById(int userId) {
        if (noExists(userId)) {
            log.debug("Проверка Пользователя c id = " + userId);
            throw new NotFoundException("Пользователя с id:%s " + userId + " не обнаружено");
        }
        String sql = "SELECT * from USERS where USER_ID = ?";
        log.info("Пользователь " + userId + " был получен");
        return jdbcTemplate.queryForObject(sql, this::makeUserDb, userId);
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

    private int makeUserInDb(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users").usingGeneratedKeyColumns("user_id");
        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    private User makeUserDb(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .name(resultSet.getString("user_name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(LocalDate.parse(resultSet.getString("birthday")))
                .friendsId(new HashSet<>(friendsDao.getFriendsIdList(resultSet.getInt("user_id")))).build();
    }

    private boolean noExists(int id) {
        String sql = "select count(*) from USERS where USER_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result == 0;
    }

    @Override
    public void deleteUser(int userId) {
        if (noExists(userId)) {
            log.debug("User id:{}", userId);
            throw new NotFoundException("Id not found");
        }
        String sql = "DELETE from USERS where USER_ID = ?";
        jdbcTemplate.update(sql, userId);
        log.info("Пользователь " + userId + " был удален");
    }

    @Override
    public void deleteAllUsers() {
        String sql = "DELETE FROM USERS";
        jdbcTemplate.update(sql);
        log.debug("Все пользователи были удалены");
    }
}
