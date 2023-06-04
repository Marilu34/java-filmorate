package org.example.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.storage.film.storage.FilmStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.example.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Service("InMemoryFilmService")
@Getter
@Slf4j
public class FilmServiceInMemory implements FilmService {
    private final FilmStorage filmStorage;

    public FilmServiceInMemory(@Qualifier("dbFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    private void checkId(int filmId, int userId) {
        log.debug("check user {} check film {}", userId, filmId);
        if (filmId <= 0 ||  userId <= 0) {
            throw new NotFoundException(String.format("User with id:%s or film with id:%s not found", userId, filmId));
        }
        if (!filmStorage.getAllFilms().contains(filmStorage.getFilmById(filmId))) {
            throw new NotFoundException(String.format("Film with id:%s not found", filmId));
        }
    }

    @Override
    public void addFilmLike(int filmId, int userId) {
        checkId(filmId, userId);
        log.debug("User {} likes film {}", userId, filmId);
        filmStorage.getFilmById(filmId).getUsersLike().add(userId);
    }

    @Override
    public void deleteFilmLike(int filmId, int userId) {
        checkId(filmId, userId);
        if (!filmStorage.getFilmById(filmId).getUsersLike().contains(userId)) {
            log.debug("user {} deleted like film {}", userId, filmId);
            throw new NotFoundException(String.format("User with id:%s not like film with id:%s"
                    , userId, filmId));
        }
        log.debug("user {} deleted like film {}", userId, filmId);
        filmStorage.getFilmById(filmId).getUsersLike().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((p0, p1) -> p1.getUsersLike().size() - p0.getUsersLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
