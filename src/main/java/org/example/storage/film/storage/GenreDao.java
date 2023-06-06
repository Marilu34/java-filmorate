package org.example.storage.film.storage;

import org.example.model.Film;
import org.example.model.Genres;

import java.util.Collection;

public interface GenreDao {

    void addFilmsGenres(Film film);

    void updateFilmsGenres(Film film);

    Genres getGenreFromDb(int genreId);

    Collection<Genres> getAllGenres();
}
