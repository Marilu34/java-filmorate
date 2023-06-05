package org.example.service;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotFoundException;
import org.example.storage.film.Db.LikeDaoDb;
import org.example.storage.film.storage.LikeDao;
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
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeDao filmLikeDao;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage, LikeDaoDb filmLikeDaoImp) {
        this.filmStorage = filmStorage;
        this.filmLikeDao = filmLikeDaoImp;
    }
    private void check(int filmId, int userId) {
        log.debug("Проверка Пользователя {} Проверка Фильма {}", userId, filmId);
        if ( filmId <= 0 || userId <= 0) {
            throw new NotFoundException(String.format("Пользователя с id:%s или Фильма с id:%s не обнаружено",
                    userId, filmId));
        }
    }

    public void addFilmLike(int filmId, int userId) {
        check(filmId, userId);
        log.debug("Пользователю {} понравился Фильм {}", userId, filmId);
        filmLikeDao.addLike(filmId, userId);
    }

    public void deleteFilmLike(int filmId, int userId) {
        check(filmId, userId);
        filmLikeDao.deleteLike(filmId, userId);
    }
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((p0, p1) -> p1.getUsersLike().size() - p0.getUsersLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
