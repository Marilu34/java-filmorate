package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Genre;
import org.example.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> getAllGenre() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{genreId}")
    public Genre getGenre(@PathVariable("genreId") int genreId) {
        return genreService.getGenre(genreId);
    }
}
