package org.example.storage.film;

import org.example.MPA.MPA;
import org.example.model.Film;

import java.sql.SQLException;
import java.util.List;


public interface FilmStorage {

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);



    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    Film getFilmById(int filmId) throws SQLException;

}