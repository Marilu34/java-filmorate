package org.example.service;

import org.example.model.Film;
import org.example.storage.film.storage.FilmStorage;

import java.util.List;

public interface FilmService {
    void addFilmLike(int filmId, int userId);

    void deleteFilmLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    FilmStorage getFilmStorage();
}
