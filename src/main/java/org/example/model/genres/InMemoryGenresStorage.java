package org.example.model.genres;

import lombok.Data;
import org.example.model.Genre;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class InMemoryGenresStorage implements GenresStorage {
    private static final List <Genre> genres = new ArrayList<>();

    @Override
    public List<Genre> getAllGenres() {
        return new ArrayList<>(genres);
    }

    @Override
    public Genre getGenreById(int genreID) {
        return genres.get(genreID);
    }

}
