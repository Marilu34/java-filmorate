package org.example.service;

import lombok.Data;
import org.example.model.Genre;
import org.example.model.genres.GenresStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class GenresService {
    private final GenresStorage genresStorage;

    @Autowired
    public GenresService(GenresStorage genresStorage) {
        this.genresStorage = genresStorage;
    }

    public List<Genre> getAllGenres() {
        return genresStorage.getAllGenres();
    }

    public Genre getGenreByID(int id) {
        return genresStorage.getGenreById(id);
    }

}