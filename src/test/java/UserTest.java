import org.example.controller.UserController;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private final UserController userController = new UserController();

    @Test
    public void testUserValidateRightCreation() {
        User user = new User(3, "yandex@ya.ru", "yandex", "Test", LocalDate.of(2000, 1, 1));
        assertEquals(user, userController.create(user));
    }

    @Test
    public void testUserWithoutName() {
        User user = new User(33, "yandex@ya.ru", "", "yandex", LocalDate.of(2000, 1, 1));
        userController.create(user);
        assertEquals(user.getName(), user.getLogin());
    }
}
