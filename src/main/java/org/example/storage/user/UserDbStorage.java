package org.example.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Getter
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("users_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        log.info("Создан Пользователь = {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (noExists(user.getId())) {
            String sqlQuery = "UPDATE USERS SET " +
                    "email = ?, login = ?, name = ?, birthday = ? " +
                    "WHERE users_id = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.info("Пользователь {} обновен", user);
            return user;
        } else {
            throw new NotFoundException(String.format("Пользователь с ID = {} не может быть обновлен", user.getId()));
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("users_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(), new HashSet<>())
        );
    }

    @Override
    public User deleteUser(User user) {
        if (noExists(user.getId())) {
            String sql = "DELETE FROM USERS WHERE users_id = ?";
            jdbcTemplate.update(sql, user.getId());
            return user;
        } else throw new NotFoundException(String.format("Пользователь с Id = {} не может быть удален", user.getId()));
    }

    @Override
    public List<User> getFriendsList(int userId) {
        if (noExists(userId)) {
            log.debug("Пользователь с Id ={}", userId);
            throw new NotFoundException("не найден Пользователь с Id = " + userId);
        }
        String sql = "select FRIEND_ID from FRIENDS where USERS_ID = ?";
        return jdbcTemplate.queryForList(sql, User.class, userId);
    }

@Override
    public List<User> getCommonFriends(int userId, int userIdToCompare) {
        String sqlQuery = "SELECT * " +
                "FROM USERS " +
                "WHERE USERS_ID IN " +
                "(SELECT * FROM (SELECT USERS_ID " +
                "FROM FRIENDS " +
                "WHERE FRIEND_ID = ? OR FRIEND_ID = ? )  GROUP BY USERS_ID HAVING COUNT(USERS_ID) > 1)";
        return jdbcTemplate.query(con -> {
                    PreparedStatement ps = con.prepareStatement(sqlQuery);
                    ps.setInt(1, userId);
                    ps.setInt(2, userIdToCompare);
                    return ps;
                }, (rs, rowNum) -> new User(
                        rs.getInt("users_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate(),
                        new HashSet<>())
        );
    }
    @Override
    public User getUser(int userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE users_id = ?", userId);
        if (userRows.first()) {
            User user = new User(
                    userRows.getInt("users_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate(), new HashSet<>()
            );
            log.info("Получен Пользователь с id = {}", userId);
            return user;
        } log.debug("User id:{}", userId);
        throw new NotFoundException("Id not found");
    }

    private boolean noExists(int id) {
        String sql = "select count(*) from USERS where USERS_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result == 0;
    }

    public void addFriend(int userId, int friendId) {
        if (!(noExists(userId) || noExists(friendId))) {
            throw  new NotFoundException("Дружба пользователей не существует");
        }
        String sql = "INSERT INTO FRIENDS (users_id, friend_id, confirmation) VALUES (? ,? , false)";
        jdbcTemplate.update(sql, userId, friendId);
        friendsStatus(userId, friendId);
        log.debug("Пользователь с id = {} добавил Пользователя с id = {} в друзья", userId, friendId);
    }
    private void friendsStatus(int userId, int friendUserId) {
        String sql = "select count(*) from FRIENDS where FRIEND_ID = ? and USERS_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, friendUserId);
        if (result == 1) {
            sql = "update FRIENDS set confirmation = true where USERS_ID = ? and FRIEND_ID = ?";
            jdbcTemplate.update(sql, userId, friendUserId);
            jdbcTemplate.update(sql, friendUserId, userId);
        }
    }
    public void deleteFriend(int userId, int friendId) {
        if (noExists(userId) || noExists(friendId)) {
            String sql = "DELETE FROM FRIENDS WHERE users_id = ? AND friend_id = ?";
            jdbcTemplate.update(sql, userId, friendId);
        } else throw new NotFoundException("Дружба пользователей не существует");
    }
}