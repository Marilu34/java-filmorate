package org.example.service;

import org.example.model.Genre;
import org.example.storage.film.storage.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Collection<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Genre getGenre(int genreId) {
        return genreDao.getGenreFromDb(genreId);
    }
}
