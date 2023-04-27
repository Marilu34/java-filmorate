package org.example.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class User {
    @NotNull
    private Integer id;
    @Email(message = "email должно содержать символы или цифры")
    String email;
    private String name;
    @NotBlank(message = "Логин не может быть пустым!")
    private String login;

    @Past
    @NonNull
    private LocalDate birthday;


}