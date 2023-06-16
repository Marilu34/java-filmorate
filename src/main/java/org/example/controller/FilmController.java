package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.AlreadyExistException;
import org.example.exceptions.NotFoundException;
import org.example.model.Film;


import org.example.service.FilmService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Validated @RequestBody Film film) {
        checkFilm(film.getId(), false);
        log.info("Фильм " + film.getName() + " с id =" + film.getId() + " создан");
        return filmService.getFilmStorage().createFilm(film);
    }


    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getFilmStorage().getAllFilms();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        checkFilm(film.getId(), true);
        log.info("Фильм " + film.getName() + " с id =" + film.getId() + " обновлен");
        return filmService.getFilmStorage().updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") int filmId) {
        checkFilm(filmId, true);
        return filmService.getFilmStorage().getFilmById(filmId);
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmService.getFilmStorage().deleteAllFilms();
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable("filmId") int filmId) {
        checkFilm(filmId, true);
        log.info("Фильм с id =" + filmId + " удален");
        filmService.getFilmStorage().deleteFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        checkFilm(filmId, true);
        filmService.addFilmLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int filmId, @PathVariable int userId) {
        checkFilm(filmId, true);
        filmService.deleteFilmLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        return filmService.getPopularFilms(count);
    }

    private void checkFilm(int filmId, boolean isValid) {
        if (isValid) {
            if (filmService.getFilmStorage().getFilmById(filmId) == null) {
                throw new NotFoundException("Фильм с id =" + filmId + " не найден");
            }
        } else if (filmId != 0 && filmService.getFilmStorage().getFilmById(filmId) != null) {
            throw new AlreadyExistException("Фильм с id =" + filmId + " уже создан");
        }
    }
}
