package org.example.service;

import org.example.model.Mpa;
import org.example.storage.film.storage.MpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collection;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getMpaRating(int mpaId) {
        return mpaDao.getMpaFromDb(mpaId);
    }

    public Collection<Mpa> getAllMpaRatings() {
        return mpaDao.getAllMpa();
    }
}
