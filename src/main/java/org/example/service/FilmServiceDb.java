package org.example.service;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.storage.film.database.FilmLikeDaoImp;
import org.example.storage.film.storage.FilmLikeDao;
import org.example.storage.film.storage.FilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.example.model.Film;

@Service("DbFilmService")
@Getter
@Slf4j
public class FilmServiceDb implements FilmService {
    private final FilmStorage filmStorage;
    private final FilmLikeDao filmLikeDao;

    @Autowired
    public FilmServiceDb(@Qualifier("dbFilmStorage") FilmStorage filmStorage, FilmLikeDaoImp filmLikeDaoImp) {
        this.filmStorage = filmStorage;
        this.filmLikeDao = filmLikeDaoImp;
    }
    private void checkId(int filmId, int userId) {
        log.debug("check user {} check film {}", userId, filmId);
        if ( filmId <= 0 || userId <= 0) {
            throw new NotFoundException(String.format("User with id:%s or film with id:%s not found",
                    userId, filmId));
        }
    }

    @Override
    public void addFilmLike(int filmId, int userId) {
        checkId(filmId, userId);
        log.debug("User {} likes film {}", userId, filmId);
        filmLikeDao.addLike(filmId, userId);
    }

    @Override
    public void deleteFilmLike(int filmId, int userId) {
        checkId(filmId, userId);
        filmLikeDao.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((p0, p1) -> p1.getUsersLike().size() - p0.getUsersLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
