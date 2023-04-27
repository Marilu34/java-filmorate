import org.example.controller.FilmController;

import org.example.exceptions.ValidationException;
import org.example.model.Film;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FilmTests {
    private final FilmController filmController = new FilmController();

    @Test
    public void testFilmRightCreation() {
        Film film = new Film(12, "Film1", "Корректная дата релиза",
                LocalDate.of(2222, 12, 2), 22);
        assertEquals(film, filmController.create(film));
    }

    @Test
    public void testFilmInCorrectDateRelease() {
        Film film = new Film(2, "Film2", "Некорректная дата релиза",
                LocalDate.of(1000, 11, 1), 11);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
}

