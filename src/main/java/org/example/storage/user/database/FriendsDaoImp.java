package org.example.storage.user.database;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.FriendException;
import org.example.storage.user.storage.FriendsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class FriendsDaoImp implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> getFriends(int userId) {
        if (noExists(userId)) {
            log.debug("user id {}", userId);
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }
        String sql = "select FRIEND_ID from FRIENDS where USER_ID = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    @Override
    public void addFriend(int userId, int friendUserId) {
        checkEqualityIdAndExists(userId, friendUserId);
        String sql = "insert into FRIENDS (USER_ID, FRIEND_ID, FRIEND_STATUS) VALUES (? ,? , false)";
        jdbcTemplate.update(sql, userId, friendUserId);
        checkAndChangeFriendsStatus(userId, friendUserId);
        log.debug("user {} friend with user {}", userId, friendUserId);
    }

    @Override
    public void deleteFriend(int userId, int friendUserId) {
        checkEqualityIdAndExists(userId, friendUserId);
        String sql = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendUserId);
        log.debug("user {} delete friend {}", userId, friendUserId);
    }

    private void checkEqualityIdAndExists(int userId, int friendUserId) {
        if (userId == friendUserId) {
            log.debug("user {} friend id {}", userId, friendUserId);
            throw new FriendException(String.format("user id %s = friends id %s", userId, friendUserId));
        }
        if (noExists(userId) || noExists(friendUserId)) {
            log.debug("User id {} friend id {}", userId, friendUserId);
            throw new NotFoundException(String.format("Users with id %s, %s not found", userId, friendUserId));
        }
    }

    private void checkAndChangeFriendsStatus(long userId, long friendUserId) {
        String sql = "select count(*) from FRIENDS where FRIEND_ID = ? and USER_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, friendUserId);
        if (result == 1) {
            sql = "update FRIENDS set FRIEND_STATUS = true where USER_ID = ? and FRIEND_ID = ?";
            jdbcTemplate.update(sql, userId, friendUserId);
            jdbcTemplate.update(sql, friendUserId, userId);
        }
    }

    @Override
    public List<Integer> getUserAllFriendsId(int userId) {
        if (noExists(userId)) {
            log.debug("user id {}", userId);
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }
        String sql = "select FRIEND_ID from FRIENDS where USER_ID = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    private boolean noExists(int id) {
        String sql = "select count(*) from USERS where USER_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result == 0;
    }
}