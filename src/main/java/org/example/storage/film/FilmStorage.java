package org.example.storage.film;

import org.example.model.Film;

import java.util.ArrayList;


public interface FilmStorage {

    ArrayList<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    ArrayList<Film> getPopularFilms(int count);

    Film getFilmById(int filmId);
}