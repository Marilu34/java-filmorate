
import lombok.RequiredArgsConstructor;
import org.example.FilmorateApplication;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.model.User;
import org.example.storage.user.Db.FriendsDbDao;
import org.example.storage.user.Db.UserDbStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserFilmoRateApplicationTests {


    private final UserDbStorage userStorage;
    private final FriendsDbDao friendsDbDao;


    @Test
    public void testFindUserById() {
       User user = new User(1, "Test1@user", "Test1", "User1", LocalDate.of(2000, 11, 11), new HashSet<>());

        userStorage.createUser(user);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("id", 1)
                );
    }


    @Test
    public void createUser() throws ValidationException {
        userStorage.createUser( new User(2, "Test2@user", "Test2", "User2", LocalDate.of(2000, 11, 11), new HashSet<>()));

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(2));
        assertTrue(userOptional.isPresent());
    }

    @Test
    public void testUpdateUser() {
        // Создаем фильм и сохраняем его в хранилище
        User user3 = new User(3, "Test3@user", "Test3", "User3", LocalDate.of(2000, 11, 11), new HashSet<>());

        User createdUser = userStorage.createUser(user3);

        // Создаем объект Пользователя

        User updatedUser = User.builder()
                .id(createdUser.getId())
                .email("email3@ya.ru")
                .login("login3")
                .name("Name3")
                .birthday(LocalDate.of(1990, 11, 11))
                .build();

        // Обновляем Пользоватля в хранилище и проверяем успешность операции
        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(updatedUser));
        assertTrue(userOptional.isPresent());
        assertEquals(updatedUser, userOptional.get());
    }

    @Test
    public void testDeleteUser() {
        userStorage.deleteUser(1);
        assertThrows(NotFoundException.class, () -> userStorage.getUserById(1));
    }
    @Test
    public void testDeleteAllUsers() {
        userStorage.deleteAllUsers();
        assertTrue(userStorage.getAllUsers().isEmpty());
    }
    @Test
    public void testAddFriend() {
        User friend = new User(4, "Test4@user", "Test4", "User4", LocalDate.of(1999, 2, 1), new HashSet<>());
        User user4 = new User(5, "Test5@user", "Test5", "User5", LocalDate.of(2000, 11, 11), new HashSet<>());

        userStorage.createUser(user4);
        userStorage.createUser(friend);
        friendsDbDao.addFriend(1, 2);
        assertTrue(userStorage.getUserById(1).getFriendsId().contains(friendsDbDao.getFriendsIdList(1).get(0)));
    }
    @Test
    public void testGetFriend() {
        User friend = new User(11, "Test4@user", "Test4", "User4", LocalDate.of(1999, 2, 1), new HashSet<>());
        User user4 = new User(12, "Test5@user", "Test5", "User5", LocalDate.of(2000, 11, 11), new HashSet<>());

        userStorage.createUser(user4);
        userStorage.createUser(friend);
       friendsDbDao.addFriend(1, 2);
        assertTrue(userStorage.getUserById(1).getFriendsId().contains(friendsDbDao.getFriendsIdList(1).get(0)));
    }
}
