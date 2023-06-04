package org.example.storage.film.database;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.model.Film;
import org.example.model.Genres;
import org.example.storage.film.storage.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Slf4j
public class GenreDaoImp implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genres getGenreFromDb(int genreId) {
        if (!exist(genreId)) {
            log.debug("getting genre with incorrect id {}", genreId);
            throw new NotFoundException(String.format("Genre with id:%s not found", genreId));
        }
        String sql = "select genre_id, genre from genres where genre_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, genreId);
    }

    @Override
    public Collection<Genres> getAllGenres() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    private Genres mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genres.builder().id(resultSet.getInt("genre_id")).name(resultSet.getString("genre"))
                .build();
    }

    @Override
    public void addFilmsGenres(Film film) {
        String sql = "insert into films_genres (film_id, genre_id) " +
                "values (?, ?)";
        for (Genres genre : film.getGenres()) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public void updateFilmsGenres(Film film) {
        deleteFromFilmsGenres(film);
        addFilmsGenres(film);
    }

    private boolean exist(int genreId) {
        String sql = "select count(*) from Genres where GENRE_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, genreId);
        return result == 1;
    }

    private void deleteFromFilmsGenres(Film film) {
        String sql = "delete from FILMS_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());

    }
}
