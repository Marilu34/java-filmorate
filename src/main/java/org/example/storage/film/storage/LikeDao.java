package org.example.storage.film.storage;

import java.util.Set;

public interface LikeDao {

    void addLike(int filmId, int userId);

    Set<Integer> getUserLikes(int filmId);

    void deleteLike(int filmId, int userId);
}
