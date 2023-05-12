package org.example.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;

    @NotNull(message = "Имя должно содержать символы")
    @NotEmpty
    private String name;

    @Size(max = 200, message = "вместимость описания до 200 символов")
    private String description;

    @Past(message = "дата выпуска не должна быть будущей")
    private LocalDate releaseDate;

    @Positive(message = "продолжительность должна быть отрицательной")
    private int duration;

    private Set<Integer> userIdLikes = new HashSet<>();
}