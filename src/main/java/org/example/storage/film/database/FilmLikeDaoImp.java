package org.example.storage.film.database;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.storage.film.storage.FilmLikeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FilmLikeDaoImp implements FilmLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikeDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Integer> getUserLikes(int filmId) {
        String sql = "select user_id from films_likes where film_id = ?";
        List<Integer> usersLike = jdbcTemplate.queryForList(sql, Integer.class, filmId);
        return new HashSet<>(usersLike);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "insert into FILMS_LIKES (film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.debug("add like to film {} from user {}", filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        checkFilmId(filmId);
        String sql = "delete from FILMS_LIKES where USER_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(sql, userId, filmId);
        log.debug("delete film {} like from user{}", filmId, userId);
    }

    private void checkFilmId(int id) {
        String sql = "select count(*) from FILMS_LIKES where FILM_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (result != 1) {
            throw new NotFoundException(String.format("film with id:%s not found", id));
        }
    }
}
