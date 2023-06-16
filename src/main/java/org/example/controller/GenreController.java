package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Genres;
import org.example.service.GenreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;


    @GetMapping
    public Collection<Genres> getAllGenre() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{genreId}")
    public Genres getGenreById(@PathVariable("genreId") int genreId) {
        return genreService.getGenreById(genreId);
    }
}
