package org.example.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.AlreadyExistException;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import org.example.storage.film.storage.FilmStorage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private static final LocalDate FIRST_FILM_RELEASE = LocalDate.of(1895, 12, 28);
    private int id = 1;

    private int generateID() {
        return id++;
    }

    @Override
    public Film addFilm(Film film) {
        if (filmMap.containsKey(film.getId())) {
            log.debug("Film id:{}", film.getId());
            throw new AlreadyExistException("Id already use");
        }
        film.setUsersLike(new HashSet<>());
        validate(film);
        film.setId(generateID());
        filmMap.put(film.getId(), film);
        log.info("film with id:{} create", film.getId());
        return film;
    }

    @Override
    public void deleteFilm(int filmId) {
        if (!filmMap.containsKey(filmId)) {
            throw new NotFoundException(String.format("Film with id:%s not found", filmId));
        }
        filmMap.remove(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmMap.containsKey(film.getId())) {
            log.debug("Film id:{}", film.getId());
            throw new NotFoundException("Id not found");
        }
        if (film.getUsersLike() == null) {
            film.setUsersLike(filmMap.get(film.getId()).getUsersLike());
        }
        validate(film);
        filmMap.put(film.getId(), film);
        log.info("Film with id:{} update", film.getId());
        return film;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE)) {
            log.debug("film not valid release date:{}", film.getReleaseDate());
            throw new ValidationException("Release date not valid");
        }
    }

    @Override
    public Film findFilmById(int filmId) {
        if (!filmMap.containsKey(filmId)) {
            throw new NotFoundException(String.format("film with id:%s not found", filmId));
        }
        return filmMap.get(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmMap.values();
    }

    @Override
    public void deleteAllFilms() {
        filmMap.clear();
    }
}
