package org.example.storage.film.Db;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import org.example.model.Genres;
import org.example.storage.film.storage.LikeDao;
import org.example.storage.film.storage.FilmStorage;
import org.example.storage.film.storage.GenreDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("dbFilmStorage")
@Getter
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDaoDb mpaDao;
    private final GenreDao genreDao;
    private final LikeDao filmLikeDao;
    private static final LocalDate FIRST_FILM_RELEASE = LocalDate.of(1895, 12, 28);


    @Override
    public Film createFilm(Film film) {
        validate(film);
        if (film.getMpa() != null) {
            film.setMpa(mpaDao.getMpaFromDb(film.getMpa().getId()));
        }
        film.setId((makeFilmInDb(film)));
        if (film.getGenres() != null) {
            genreDao.addFilmsGenres(film);
        }
        log.info("Фильм создан: {}", film);
        return film;
    }

    @Override
    public void deleteFilm(int filmId) {
        if (noExists(filmId)) {
            log.debug("Ошибка при удалении фильма {}", filmId);
            throw new NotFoundException("Не обнаружен Фильм с id: {}" + filmId);
        }
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        if (noExists(film.getId())) {
            log.debug("Ошибка при обновлении фильма {}", film.getId());
            throw new NotFoundException("Не обнаружен Фильм с id: {}" + film.getId());
        }
        if (film.getGenres() != null) {
            genreDao.updateFilmsGenres(film);
        }
        String sql = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, FILM_RATE = ?, MPA_ID = ? " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId(), film.getId());
        return getFilmById(film.getId());
    }


    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from films;";
        return jdbcTemplate.query(sql, this::makeFilmDb);
    }

    @Override
    public void deleteAllFilms() {
        String sql = "delete from films";
        jdbcTemplate.update(sql);
    }

    @Override
    public Film getFilmById(int filmId) {
        if (noExists(filmId)) {
            log.debug("Ошибка при получении фильма {}", filmId);
            throw new NotFoundException(String.format("Не обнаружен Фильм с id: {}" + filmId));
        }
        String sql = "select * from films where film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeFilmDb, filmId);
    }

    private int makeFilmInDb(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films").usingGeneratedKeyColumns("film_id");
        return simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
    }


    private void validate(@Validated Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE)) {
            throw new ValidationException("Дата выпуска Film недействительна");
        }
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Имя Film не может быть пустым");
        }
        if (film.getDuration() <= 0 || film.getDuration() > 200) {
            throw new ValidationException("Продолжительность Film не может быть отрицательным");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            throw new ValidationException("Описание Film не может быть больше 200 символов");
        }
    }

    private Film makeFilmDb(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(LocalDate.parse(resultSet.getString("release_date")))
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDao.getMpaFromDb(resultSet.getInt("mpa_id")))
                .rate(resultSet.getInt("film_rate"))
                .genres(getGenres(resultSet.getInt("film_id")))
                .usersLike(filmLikeDao.getUserLikes(resultSet.getInt("film_id"))).build();
    }

    private Set<Genres> getGenres(int rowNum) {
        Set<Genres> genres = new HashSet<>();
        String sql = "select GENRE_ID from FILMS_GENRES where FILM_ID = ?";
        List<Integer> listGenreId = jdbcTemplate.queryForList(sql, Integer.class, rowNum);
        for (int genreId : listGenreId) {
            genres.add(genreDao.getGenreFromDb(genreId));
        }
        return genres;
    }

    private boolean noExists(long id) {
        String sql = "select count(*) from FILMS where FILM_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result == 0;
    }
}
