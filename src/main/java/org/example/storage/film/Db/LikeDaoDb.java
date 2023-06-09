package org.example.storage.film.Db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.storage.film.storage.LikeDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeDaoDb implements LikeDao {
    private final JdbcTemplate jdbcTemplate;


    private void check(int id) {
        String sql = "select count(*) from LIKES where FILM_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (result != 1) {
            throw new NotFoundException(String.format("Фильм с id:%s не обнаружен", id));
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "insert into LIKES (film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.debug(" Фильм {} понравился  Пользователю {}", filmId, userId);
    }

    @Override
    public Set<Integer> getUserLikes(int filmId) {
        String sql = "select user_id from likes where film_id = ?";
        List<Integer> usersLike = jdbcTemplate.queryForList(sql, Integer.class, filmId);
        return new HashSet<>(usersLike);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        check(filmId);
        String sql = "delete from LIKES where USER_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(sql, userId, filmId);
        log.debug(" Фильм {} больше не нравится Пользователю {}", filmId, userId);
    }
}
