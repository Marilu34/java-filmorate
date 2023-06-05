package org.example.controller;

import org.example.model.Mpa;
import org.example.service.MpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class RatingController {
    private final MpaService mpaService;

    @Autowired
    public RatingController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> getAllRatings() {
        return mpaService.getAllMpaRatings();
    }

    @GetMapping("/{mpaId}")
    public Mpa getRatingById(@PathVariable("mpaId") int mpaId) {
        return mpaService.getRatingById(mpaId);
    }
}