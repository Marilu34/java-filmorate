package org.example.controller;

import lombok.Data;
import org.example.exceptions.IDException;
import org.example.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Film;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
@Data
@Validated

public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;
    private static final LocalDate FIRST_FILM_RELEASE = LocalDate.of(1895, 12, 28);

    private Integer generateID() {
        return id++;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        if (films.containsKey(film.getId())) {
            log.debug("Film с id:{}", film.getId());
            throw new IDException("Id уже занят");
        }
        film.setId(generateID());
        validate(film);
        films.put(film.getId(), film);
        log.info("Создан Film с id:{}", film.getId());
        return film;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE)) {
            log.debug("Дата выпуска Film :{}", film.getReleaseDate());
            throw new ValidationException("Дата выпуска Film недействительна");
        }
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Имя Film не может быть пустым");
        }
        if (film.getDuration() <= 0 || film.getDuration() > 200) {
            throw new ValidationException("Продолжительность Film не может быть отрицательным");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            throw new ValidationException("Описание Film не может быть больше 200 символов");
        }
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PutMapping
    public Film put(@RequestBody @Valid Film film) {
        if (((id == 0) || (id < 0)) | (film.getName() == null)) {
            throw new ValidationException("id должен быть больше 0");
        }
        if (!films.containsKey(film.getId())) {
            log.debug("Film с id:{}", film.getId());
            throw new IDException("Id не обнаружен");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Film с id:{} обновлен", film.getId());
        return film;
    }
}