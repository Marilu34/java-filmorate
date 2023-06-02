package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Genre implements Comparable<Genre> {
    private Integer genreID;
    private String genre_name;

    @Override
    public int compareTo(Genre compaGenre) {
        return this.genreID.compareTo(compaGenre.getGenreID());
    }
}
