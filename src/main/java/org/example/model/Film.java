package org.example.model;

import lombok.*;
import org.example.MPA.MPA;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    private Set<Genre> genre;
    @NotNull private MPA mpa;
    private int rate;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_name", name);
        values.put("film_description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", mpa.getId());
        values.put("film_rate", rate);
        return values;
    }
}