package org.example.storage.film.storage;

import org.example.model.Mpa;

import java.util.Collection;

public interface MpaDao {
    Collection<Mpa> getAllMpa();

    Mpa getMpaFromDb(int mpaId);
}
