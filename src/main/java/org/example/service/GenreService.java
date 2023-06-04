package org.example.service;

import org.example.model.Genres;
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

    public Collection<Genres> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Genres getGenre(int genreId) {
        return genreDao.getGenreFromDb(genreId);
    }
}
