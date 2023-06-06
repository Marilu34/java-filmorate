import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.assertj.core.api.AssertionsForClassTypes;
import org.example.controller.FilmController;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import org.example.model.Mpa;
import org.example.model.User;
import org.example.service.FilmService;
import org.example.storage.user.storage.UserStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmoRateApplicationTests.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
   FilmController filmController;
    private final User user = new User(1, "yandex@ya.ru", "yandex", "Test",
            LocalDate.of(2000, 1, 1), new HashSet<>());
    private final Film film = new Film(1, "Test film", "Test", LocalDate.of(2000, 1, 1),
            100, new HashSet<>(), new HashSet<>(), Mpa.builder().id(1).build(), 3);

     @Test
    public void createFilm() throws ValidationException {
        filmController.createFilm(film);
        assertEquals(filmController.getAllFilms().size(), 1);
    }
}