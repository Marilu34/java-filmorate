import lombok.RequiredArgsConstructor;
import org.example.FilmorateApplication;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.Film;
import org.example.model.Genres;
import org.example.model.Mpa;
import org.example.model.User;
import org.example.storage.film.Db.FilmDbStorage;
import org.example.storage.film.Db.LikeDaoDb;
import org.example.storage.film.Db.MpaDaoDb;
import org.example.storage.user.Db.FriendsDbDao;
import org.example.storage.user.Db.UserDbStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class FilmoRateApplicationTests {

    private final UserDbStorage userStorage;
    private final FriendsDbDao friendsDbDao;

    private final FilmDbStorage filmStorage;

    private final LikeDaoDb likeStorage;

    private final MpaDaoDb mpaStorage;
    private static final User user = new User(2, "Test2@user", "Test2", "User2", LocalDate.of(2000, 11, 11), new HashSet<>());


    private static final Film film = new Film(1, "Test film", "Test", LocalDate.of(2000, 1, 1),
            100, new HashSet<>(), new HashSet<>(), Mpa.builder().id(1).name("G").build(), 3);
    @BeforeEach
    public void deleteUser() {
        if(!userStorage.getAllUsers().isEmpty()) {
            userStorage.deleteAllUsers();
        }
        if (!filmStorage.getAllFilms().isEmpty()) {
            filmStorage.deleteAllFilms();
        }
    }


    @Test
    public void createUser() throws ValidationException {
        userStorage.createUser(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(7));
        assertTrue(userOptional.isPresent());
    }

    @Test
    public void testFindUserById() {

        userStorage.createUser(user);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(6));
       assertThat(userOptional).isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("id", 6));
    }

    @Test
    public void testUpdateUser() {
        User createdUser = userStorage.createUser(user);
        User updatedUser = User.builder()
                .id(createdUser.getId())
                .email("email3@ya.ru")
                .login("loginfv3")
                .name("Namedv3")
                .birthday(LocalDate.of(1990, 11, 11))
                .build();
        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(updatedUser));
        assertTrue(userOptional.isPresent());
        assertEquals(updatedUser, userOptional.get());
    }
    @Test
    public void testDeleteUser() {
      //  userStorage.deleteUser(1);
        assertThrows(NotFoundException.class, () -> userStorage.getUserById(1));
    }
    @Test
    public void testDeleteAllUsers() {
        userStorage.deleteAllUsers();
        assertTrue(userStorage.getAllUsers().isEmpty());
    }
    @Test
    public void testAddAndGetFriend() {
        User user1 = userStorage.createUser(user);
        User friend = User.builder()
                .id(44)
                .email("email3@ya.ru")
                .login("loginfv3")
                .name("Namedv3")
                .birthday(LocalDate.of(1990, 11, 11))
                .build();
        userStorage.createUser(friend);
        friendsDbDao.addFriend(user1.getId(), friend.getId());
        assertTrue(friendsDbDao.getFriendsIdList(user1.getId()).contains(friend.getId()));
    }
    @Test
    public void testDeleteFriend() {
        User user1 = userStorage.createUser(user);
        User friend = User.builder()
                .id(4)
                .email("email3@ya.ru")
                .login("loginfv3")
                .name("Namedv3")
                .birthday(LocalDate.of(1990, 11, 11))
                .build();
        userStorage.createUser(friend);
        friendsDbDao.addFriend(user1.getId(), friend.getId());
        friendsDbDao.deleteFriend(user1.getId(), friend.getId());
        assertFalse(friendsDbDao.getFriendsIdList(user1.getId()).contains(friend.getId()));
    }


    @Test
    public void testFindFilmById() {
        filmStorage.createFilm(film);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(4));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 4)
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
        filmStorage.deleteFilm(5);
        assertTrue(filmStorage.getAllFilms().isEmpty());
    }
    @Test
    public void testGetAllMpaRatings() {
        assertEquals(5, mpaStorage.getAllMpa().size());
    }
    @Test
    public void testGetAllFilms() {
        assertTrue(filmStorage.getAllFilms().isEmpty());
    }
    @Test
    public void testGetMpaRatingsById() {
        assertEquals("G", mpaStorage.getMpaFromDb(1).getName());
    }
    @Test
    void shouldAddAndGetLikes() {
        Film film1 = filmStorage.createFilm(film);
        List<User> users = new ArrayList<>();
        userStorage.createUser(user);
    users.add(user);
    Set<Integer> emptyLikes = likeStorage.getUserLikes(film1.getId());
        assertTrue(emptyLikes.isEmpty());

        users.forEach(user -> likeStorage.addLike(film1.getId(), user.getId()));
        Set<Integer> likes = likeStorage.getUserLikes(film1.getId());

        assertEquals(likes.size(), users.size());
        users.forEach(user -> assertTrue(likes.contains(user.getId())));
    }
    @Test
    void shouldDeleteLikes() {
        Film film2 = filmStorage.createFilm(film);
        List<User> users = new ArrayList<>();
        userStorage.createUser(user);
        users.add(user);


        likeStorage.addLike(film2.getId(), users.get(0).getId());
        Set<Integer>likes = likeStorage.getUserLikes(film2.getId());
        assertEquals(likes.size(), 1);

        likeStorage.deleteLike(film2.getId(), users.get(0).getId());
        Set<Integer> emptyLikes = likeStorage.getUserLikes(film2.getId());
        assertTrue(emptyLikes.isEmpty());
    }
}
