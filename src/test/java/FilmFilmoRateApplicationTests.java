
import lombok.RequiredArgsConstructor;
import org.example.FilmorateApplication;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import org.example.model.Genres;
import org.example.model.Mpa;
import org.example.storage.film.Db.FilmDbStorage;
import org.example.storage.film.Db.GenreDaoDb;
import org.example.storage.film.Db.MpaDaoDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmFilmoRateApplicationTests {

    private final FilmDbStorage filmStorage;

   private final MpaDaoDb mpaDao;
    private final Film film = new Film(1, "Test film", "Test", LocalDate.of(2000, 1, 1),
            100, new HashSet<>(), new HashSet<>(), Mpa.builder().id(1).name("G").build(), 3);

    @Test
    public void testFindFilmById() {
        filmStorage.createFilm(film);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void createFilm() throws ValidationException {
        filmStorage.createFilm(film);
        assertEquals(filmStorage.getAllFilms().size(), 1);
    }


    @Test
    public void testUpdateFilm() {
        // Создаем фильм и сохраняем его в хранилище
        Film createdFilm = filmStorage.createFilm(film);

        // Создаем объекты жанров и обновляем поля фильма
        Set<Genres> genres = new HashSet<>();
        genres.add(Genres.builder().id(2).name("Драма").build());
        genres.add(Genres.builder().id(3).name("Мультфильм").build());
        Film updatedFilm = Film.builder()
                .id(createdFilm.getId())
                .name("newName")
                .description("newDesc")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100).usersLike(new HashSet<>())
                .genres(genres)
                .mpa(Mpa.builder().id(1).name("G").build())
                .build();

        // Обновляем фильм в хранилище и проверяем успешность операции
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.updateFilm(updatedFilm));
        assertTrue(filmOptional.isPresent());
        assertEquals(updatedFilm, filmOptional.get());
    }

    @Test
    public void testDeleteAllFilm() {
        filmStorage.deleteAllFilms();
        assertTrue(filmStorage.getAllFilms().isEmpty());
    }

    @Test
    public void testDeleteFilm() {
        filmStorage.createFilm(film);
        filmStorage.deleteFilm(3);
        assertTrue(filmStorage.getAllFilms().isEmpty());
    }
    @Test
    public void testGetAllMpaRatings() {
        assertEquals(5, mpaDao.getAllMpa().size());
    }
    @Test
    public void testGetAllFilms() {
        assertTrue(filmStorage.getAllFilms().isEmpty());
    }
    @Test
    public void testGetMpaRatingsById() {
        assertEquals("G", mpaDao.getMpaFromDb(1).getName());
    }


}
