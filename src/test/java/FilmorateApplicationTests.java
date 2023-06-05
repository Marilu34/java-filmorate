import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.controller.FilmController;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import org.example.model.Mpa;
import org.example.model.User;
import org.example.storage.film.Db.FilmDbStorage;
import org.example.storage.film.storage.GenreDao;
import org.example.storage.film.storage.MpaDao;
import org.example.storage.user.Db.UserDbStorage;
import org.example.storage.user.storage.FriendDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationTests {



    private final UserDbStorage userStorage;
    private final FriendDao friendsDao;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmDbStorage filmDbStorage;
    FilmController filmController;
    private final User user = new User(1, "yandex@ya.ru", "yandex", "Test",
            LocalDate.of(2000, 1, 1), new HashSet<>());
    private final Film film = new Film(1, "Test film", "Test", LocalDate.of(2000, 1, 1),
            100, new HashSet<>(), new HashSet<>(), Mpa.builder().id(1).build(), 3);

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testUpdateUser() {
        userStorage.createUser(user);
        User userUp = new User(1, "1@2.ru", "yandex1", "test", LocalDate.of(2000, 1, 1), new HashSet<>());
        userStorage.updateUser(userUp);
        assertEquals(userUp, userStorage.getUserById(1));
    }

    @Test
    public void testDeleteUserIncorrectId() {
        User userUp = new User(132, "1@2.ru", "yandex1", "test", LocalDate.of(2000, 1, 1), new HashSet<>());
        assertThrows(NotFoundException.class, () -> userStorage.updateUser(userUp));
    }

    @Test
    public void testDeleteUser() {
        userStorage.deleteUser(1);
        assertThrows(NotFoundException.class, () -> userStorage.getUserById(1));
    }

    @Test
    public void testDddFriend() {
        User friend = new User(1, "f@a.ru,", "f", "fa", LocalDate.of(1999, 2, 1), new HashSet<>());
        userStorage.createUser(friend);
        friendsDao.addFriend(1, 2);
        assertTrue(userStorage.getUserById(1).getFriendsId().contains(friendsDao.getFriends(1).get(0)));
    }

    @Test
    public void testAddFriendsIncorrectId() {
        assertThrows(NotFoundException.class, () -> friendsDao.deleteFriend(1, 23));
    }

    @Test
    public void testGetAllFriendsIdWithIncorrectId() {
        assertThrows(NotFoundException.class, () -> friendsDao.getUserAllFriendsId(123));
    }

    @Test
    public void testGetAllMpaRatings() {
        assertEquals(5, mpaDao.getAllMpa().size());
    }

    @Test
    public void testGetMpaRatingsById() {
        assertEquals("G", mpaDao.getMpaFromDb(1).getName());
    }

    @Test
    public void testGetMpaRatingsWithIncorrectId() {
        assertThrows(NotFoundException.class, () -> mpaDao.getMpaFromDb(123));
    }

    @Test
    public void testGetAllGenres() {
        assertEquals(6, genreDao.getAllGenres().size());
    }

    @Test
    public void testGetGenreById() {
        assertEquals("Комедия", genreDao.getGenreFromDb(1).getName());
    }

    @Test
    public void testAddFilmAndReturn() {
        filmDbStorage.createFilm(film);
        assertEquals(film, filmDbStorage.getFilmById(3));
    }

    @Test
    public void testFindFilmIncorrectID() {
        assertThrows(NotFoundException.class, () -> filmDbStorage.getFilmById(123));
    }

    @Test
    public void testUpdateFilm() {
        filmDbStorage.createFilm(film);
        film.setName("Movie");
        filmDbStorage.updateFilm(film);
        assertEquals(film.getName(), filmDbStorage.getFilmById(1).getName());
    }

    @Test
    public void testUpdateFilmWithIncorrectId() {
        film.setId(32);
        assertThrows(NotFoundException.class, () -> filmDbStorage.updateFilm(film));
    }

    @Test
    public void testDeleteAllFilm() {
        filmDbStorage.deleteAllFilms();
        assertTrue(filmDbStorage.getAllFilms().isEmpty());
    }

    @Test
    public void testDeleteFilm() {
        filmDbStorage.createFilm(film);
        filmDbStorage.deleteFilm(2);
        assertTrue(filmDbStorage.getAllFilms().isEmpty());
    }
    @Test
    public void createFilm() throws ValidationException {
        filmController.createFilm(film);

        assertEquals(filmController.getAllFilms().size(), 1);
    }
}