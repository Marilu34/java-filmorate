package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.AlreadyExistException;
import org.example.exceptions.NotFoundException;
import org.example.model.Film;
import org.example.service.FilmService;

import org.example.storage.film.storage.FilmStorage;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;


    public FilmController(@Qualifier("DbFilmService") FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Validated @RequestBody Film film) {
        checkFilm((int) film.getId(), false);
        log.info("Фильм " + film.getName() + " с айди =" + film.getId() + " создан");
        return filmService.getFilmStorage().addFilm(film);
    }


    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getFilmStorage().getAllFilms();
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        return filmService.getFilmStorage().updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") int filmId) {
        checkFilm(filmId, true);
        return filmService.getFilmStorage().findFilmById(filmId);
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmService.getFilmStorage().deleteAllFilms();
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable("filmId") int filmId) {
        filmService.getFilmStorage().deleteFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        filmService.addFilmLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        filmService.deleteFilmLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        return filmService.getPopularFilms(count);
    }
    private void checkFilm(int filmId, boolean isValid) {
        if (isValid) {
            if (filmService.getFilmStorage().findFilmById(filmId) == null) {
                throw new NotFoundException("Фильм с айди =" + filmId + " не найден");
            }
        } else if (filmId != 0 && filmService.getFilmStorage().findFilmById(filmId) != null) {
            throw new AlreadyExistException("Фильм с айди =" + filmId + " уже создан");
        }
    }
}
