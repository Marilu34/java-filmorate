package org.example.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@NotNull
@AllArgsConstructor
@NoArgsConstructor

public class Film {
    @NonNull
    private Integer id;
    @NotBlank(message = "Имя должно содержать символы")
    private String name;
    @NotBlank(message = "Описание должно содержать символы")
    @Size(max = 200, message = "вместимость описания до 200 символов")
    private String description;
    @Past(message = "дата выпуска не должна быть будущей")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность должна быть отрицательной")
    @NotNull
    private Integer duration;
}