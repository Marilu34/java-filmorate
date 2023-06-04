package org.example.storage.film.storage;

import org.example.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film createFilm(Film film);

    void deleteFilm(int filmId);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    void deleteAllFilms();

    Film getFilmById(int filmId);
}
