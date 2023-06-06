package org.example.storage.user.Db;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.FriendException;
import org.example.storage.user.storage.FriendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FriendsDbDao implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean noExists(int id) {
        String sql = "select count(*) from USERS where USER_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result == 0;
    }
    @Override
    public List<Integer> getFriendsIdList(int userId) {
        if (noExists(userId)) {
            throw new NotFoundException("Пользователь с id:%s не обнаружен"+ userId);
        }
        String sql = "select FRIEND_ID from FRIENDS where USER_ID = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    @Override
    public void addFriend(int userId, int friendUserId) {
        checkUsers(userId, friendUserId);
        String sql = "insert into FRIENDS (USER_ID, FRIEND_ID, FRIEND_STATUS) VALUES (? ,? , false)";
        jdbcTemplate.update(sql, userId, friendUserId);
        friendsStatus(userId, friendUserId);
        log.debug("Пользователь {} дружит с Пользователем {}", userId, friendUserId);
    }

    @Override
    public void deleteFriend(int userId, int friendUserId) {
        checkUsers(userId, friendUserId);
        String sql = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendUserId);
        log.debug("Пользователь {} удалил из друзей Пользователя {}", userId, friendUserId);
    }

    private void checkUsers(int userId, int friendUserId) {
        if (userId == friendUserId) {
            log.debug("Проверка Пользователя {} Проверка друга Пользователя {}", userId, friendUserId);
            throw new FriendException("id = " +  userId + "Пользователя = id " + friendUserId + " друга Пользователя ");
        }
        if (noExists(userId) || noExists(friendUserId)) {
            log.debug("Проверка Пользователя {} Проверка друга Пользователя {}", userId, friendUserId);
            throw new NotFoundException(String.format("Пользователи с id:%s не обнаружен", userId, friendUserId));
        }
    }

    private void friendsStatus(long userId, long friendUserId) {
        String sql = "select count(*) from FRIENDS where FRIEND_ID = ? and USER_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, userId, friendUserId);
        if (result == 1) {
            sql = "update FRIENDS set FRIEND_STATUS = true where USER_ID = ? and FRIEND_ID = ?";
            jdbcTemplate.update(sql, userId, friendUserId);
            jdbcTemplate.update(sql, friendUserId, userId);
        }
    }
}
