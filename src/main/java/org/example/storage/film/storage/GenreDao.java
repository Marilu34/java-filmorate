package org.example.storage.film.storage;

import org.example.model.Film;
import org.example.model.Genre;
import java.util.Collection;

public interface GenreDao {
    Genre getGenreFromDb(int genreId);

    Collection<Genre> getAllGenres();

    void addFilmsGenres(Film film);

    void updateFilmsGenres(Film film);
}
