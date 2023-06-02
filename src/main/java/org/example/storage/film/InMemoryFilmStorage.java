package org.example.storage.film;

import lombok.Data;
import org.example.MPA.MPA;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate FIRST_FILM_RELEASE = LocalDate.of(1895, 12, 28);
    private static int filmID = 1;
    Film film = new Film();

    private static final Map<Integer, Film> films = new HashMap<>();

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        film.setId(generateID());
      //  updateRating(MPArating);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.replace(film.getId(), film);
        return film;
    }



    @Override
    public Film getFilmById(int filmId) {
        return films.get(filmId);
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.getUserIdLikes().add(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.getUserIdLikes().remove(userId);
    }

    @Override
    public ArrayList<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted(this::compareFilms)
                .limit(count)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private int compareFilms(Film filmFirst, Film filmSecond) {
        int result = 0;
        if (filmFirst.getUserIdLikes().size() > filmSecond.getUserIdLikes().size()) {
            result = -1;
        }
        return result;
    }

    private int generateID() {
        return filmID++;
    }


    private void validate(@Validated Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE)) {
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
//        if (film.getMPArating().isBlank()) {
//            throw new ValidationException("Рейтинг Film не может быть пустым");
//        }

    }
}