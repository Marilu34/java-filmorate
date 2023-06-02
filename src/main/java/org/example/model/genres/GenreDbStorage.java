package org.example.model.genres;

import lombok.Data;
import org.example.model.Film;
import org.example.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Set;
import java.util.TreeSet;

@Data
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;
    public Set<Genre> getFilmGenres(int filmId) {
        String sql = "SELECT GENRE.GENRE_ID, GENRE FROM FILMS_GENRE JOIN GENRE " +
                "ON FILMS_GENRE.GENRE_ID = GENRE.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return new TreeSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre_name")),
                filmId
        ));
    }
    public void updateGenresOfFilm(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE film_id = ?", film.getId());
        if (film.getGenre() != null) {
            for (Genre genre : film.getGenre()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getGenreID());
            }
        }
    }
}
