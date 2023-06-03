package org.example.service;

import org.example.model.Film;
import org.example.storage.film.storage.FilmStorage;

import java.util.List;

public interface FilmService {
    void addFilmLike(long filmId, long userId);

    void deleteFilmLike(long filmId, long userId);

    List<Film> getPopularFilms(int count);

    FilmStorage getFilmStorage();
}
