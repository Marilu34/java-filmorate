package org.example.storage.film.Db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.model.Film;
import org.example.model.Genres;
import org.example.storage.film.storage.GenreDao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenreDaoDb implements GenreDao {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Genres getGenreFromDb(int genreId) {
        if (!exist(genreId)) {
            log.debug("Ошибка при получении жанра {}", genreId);
            throw new NotFoundException(String.format("Не обнаружен жанр с id: {}" + genreId));
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
        Set<Genres> genreSet = film.getGenres();
        int filmId = film.getId();
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genres genre = (Genres) genreSet.toArray()[i];
                ps.setInt(1, filmId);
                ps.setInt(2, genre.getId());
            }


            public int getBatchSize() {
                return genreSet.size();
            }
        });
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
