import org.example.model.User;
import org.example.storage.user.InMemoryUserStorage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    @Test

    public void testUserValidateRightCreation() {
        User user = new User(3, "yandex@ya.ru", "yandex", "Test", LocalDate.of(2000, 1, 1), new HashSet<>());
        assertEquals(user, userStorage.createUser(user));
    }

    @Test
    public void testUserWithoutName() {
        User user = new User(33, "yandex@ya.ru", "", "yandex", LocalDate.of(2000, 1, 1), new HashSet<>());
        userStorage.createUser(user);
        assertEquals(user.getName(), user.getLogin());
    }
}
