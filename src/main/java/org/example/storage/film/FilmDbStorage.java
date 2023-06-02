package org.example.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.MPA.MPADbStorage;
import org.example.exceptions.NotFoundException;
import org.example.model.Film;
import org.example.model.Genre;
import org.example.MPA.MPA;
import org.example.model.genres.GenreDbStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@Data
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    GenreDbStorage genreDbStorage;
    MPADbStorage mpaDbStorage;
    Film film;

    public boolean isFilmExists(int id) {
        String sql = "SELECT * FROM FILMS WHERE film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        return userRows.next();
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM FILMS ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(),
                genreDbStorage.getFilmGenres(rs.getInt("film_id")),
                makeMPA(rs,rowNum),
                rs.getInt("film_rate")
        ));

    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        if (film.getGenre() != null) {
            for (Genre genre : film.getGenre()) {
                jdbcTemplate.update("INSERT INTO FILMS_GENRES (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getGenreID());
            }
        }
        jdbcTemplate.update("UPDATE FILMS SET MPA_ID = ? WHERE FILM_ID = ?",
                film.getMpa().getId(),
                film.getId()
        );

        log.info("New film added: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (isFilmExists(film.getId())) {
            String sqlQuery = "UPDATE FILMS SET " +
                    "name = ?, description = ?, release_date = ?, duration = ?, " +
                    "MPA_id = ? WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            log.info("Film {} has been successfully updated", film);
            return film;
        } else {
            throw new NotFoundException("Не возможно обновить фильм с id = {}" + film.getId());
        }
    }


    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO LIKES (users_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}", filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM USER_LIKE WHERE users_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    private boolean isLikeExist(Long userId, Long filmId) {
        String sql = "SELECT * FROM USER_LIKE WHERE users_id = ? AND film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        return userRows.next();
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT FILMS.FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID , " +
                "COUNT(L.USER_ID) as MPA FROM FILMS LEFT JOIN USER_LIKE L on FILMS.FILM_ID = L.FILM_ID " +
                "GROUP BY FILMS.FILM_ID " +
                "ORDER BY MPA DESC LIMIT ?";
        System.out.println(count);
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(),
                genreDbStorage.getFilmGenres(rs.getInt("film_id")),
                makeMPA(rs,rowNum),
                rs.getInt("film_rate")
        ), count);
        return films;
    }
     Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("film_description"))
                .releaseDate(LocalDate.parse(resultSet.getString("release_date")))
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDbStorage.getMpa(resultSet.getInt("mpa_id")))
                .rate(resultSet.getInt("film_rate"))
                .genre(genreDbStorage.getFilmGenres((int) resultSet.getLong("film_id")))
                .userIdLikes(Collections.singleton(((int) resultSet.getLong("film_id")))).build();
    }

    @Override
    public Film getFilmById(int filmId) throws SQLException {
        if (isFilmExists(filmId)) {
            log.debug("getting film {} with incorrect id", filmId);
            throw new NotFoundException(String.format("film with id:%s not found", filmId));
        }
        String sql = "select * from films where film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, filmId);
    }

        private MPA makeMPA(ResultSet rs, int mpaId) throws SQLException {
       return MPA.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("rating"))
                .build();
    }
    private boolean ifMPANoExists(int MPAID) {
        String sql = "select count (*) from mpa_ratings where RATING_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, MPAID);
        return result == 0;
    }

    public MPA getMPA(int MPAID) {
        if (ifMPANoExists(MPAID)) {
            log.debug("getting mpa rating with incorrect id {}", MPAID);
            throw new NotFoundException(String.format("MPA rating with id:%s not found",MPAID));
        }
        String sql = "select rating_id, rating from mpa_ratings where rating_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeMPA, MPAID);
    }
}
