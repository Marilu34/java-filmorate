package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Genres;
import org.example.storage.film.storage.GenreDao;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;


    public Collection<Genres> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Genres getGenreById(int genreId) {
        return genreDao.getGenreFromDb(genreId);
    }
}
