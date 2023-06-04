package org.example.storage.film.storage;

import java.util.Set;

public interface FilmLikeDao {
    Set<Integer> getUserLikes(int filmId);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);
}
