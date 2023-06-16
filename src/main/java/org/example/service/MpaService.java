package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Mpa;
import org.example.storage.film.storage.MpaDao;
import org.springframework.stereotype.Service;


import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public Mpa getRatingById(int mpaId) {
        return mpaDao.getMpaFromDb(mpaId);
    }

    public Collection<Mpa> getAllMpaRatings() {
        return mpaDao.getAllMpa();
    }
}
