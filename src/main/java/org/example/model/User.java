package org.example.model;

import lombok.*;
import org.example.model.friendship.FriendshipStatus;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    @NotNull
    @Email(message = "email должно содержать символы или цифры")
    private String email;

    @NotNull
    @NotBlank(message = "Логин не может быть пустым!")
    private String login;

    @Nullable
    private String name;

    @Past
    private LocalDate birthday;

    private Set<Integer> friendsIds = new HashSet<>();

      public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}