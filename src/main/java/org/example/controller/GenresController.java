package org.example.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Genre;
import org.example.service.GenresService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/genres")
@Data
public class GenresController {
    private final GenresService genreService;

      @GetMapping("/allGenres")
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable("id") Integer id) {
        return genreService.getGenreByID(id);
    }
}