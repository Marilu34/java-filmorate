package org.example.service;

import org.example.model.Film;
import org.example.storage.film.FilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAllFilms();
    }

    public Film add(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(Integer id) {
        return filmStorage.getPopularFilms(id);
    }

    public Film getFilmById(Integer filmId) throws SQLException {
        return filmStorage.getFilmById(filmId);
    }
}
