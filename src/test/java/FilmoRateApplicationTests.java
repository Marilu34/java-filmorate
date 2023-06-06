import lombok.*;

import org.assertj.core.api.AssertionsForClassTypes;
import org.example.controller.FilmController;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import org.example.model.Mpa;
import org.example.model.User;
import org.example.service.FilmService;
import org.example.storage.film.Db.FilmDbStorage;
import org.example.storage.film.storage.FilmStorage;
import org.example.storage.user.storage.UserStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmoRateApplicationTests.class)
@AutoConfigureTestDatabase
@Data

class FilmoRateApplicationTests {
//@Autowired
        //        ("dbFilmStorage")

  private final FilmDbStorage filmStorage ;
  public FilmoRateApplicationTests(FilmDbStorage filmStorage) {
      this.filmStorage = filmStorage;
  }

    private final User user = new User(1, "yandex@ya.ru", "yandex", "Test",
            LocalDate.of(2000, 1, 1), new HashSet<>());
    private final Film film = new Film(1, "Test film", "Test", LocalDate.of(2000, 1, 1),
            100, new HashSet<>(), new HashSet<>(), Mpa.builder().id(1).build(), 3);

     @Test
    public void createFilm() throws ValidationException {
        filmStorage.createFilm(film);
        assertEquals(filmStorage.getAllFilms().size(), 1);
    }
    @Test
    public void testUpdateFilm() {
        filmStorage.createFilm(film);
        film.setName("Movie");
        filmStorage.updateFilm(film);
        assertEquals(film.getName(), filmStorage.getFilmById(1).getName());
    }
}