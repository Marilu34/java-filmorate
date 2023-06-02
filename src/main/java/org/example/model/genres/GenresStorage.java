package org.example.model.genres;

import org.example.model.Genre;
import java.util.List;

public interface GenresStorage {
    List<Genre> getAllGenres();
    Genre getGenreById(int id);
}
