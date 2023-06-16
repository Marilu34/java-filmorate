package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Mpa;
import org.example.service.MpaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> getAllRatings() {
        return mpaService.getAllMpaRatings();
    }

    @GetMapping("/{mpaId}")
    public Mpa getRatingById(@PathVariable("mpaId") int mpaId) {
        return mpaService.getRatingById(mpaId);
    }
}