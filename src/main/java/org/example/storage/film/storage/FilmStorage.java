package org.example.storage.film.storage;

import org.example.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film createFilm(Film film);


    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    Film getFilmById(int filmId);

    void deleteFilm(int filmId);

    void deleteAllFilms();
}
