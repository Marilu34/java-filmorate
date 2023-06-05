package org.example.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Film {
    private int id;
    @NotBlank(message = "name is blank")
    private String name;
    @NotBlank(message = "description is blank")
    @Size(max = 200, message = "max size description 200 symbols")
    private String description;
    @Past(message = "release date cannot be in future")
    private LocalDate releaseDate;
    @NotNull
    @Positive(message = "duration is negative")
    private Integer duration;

    private Set<Integer> usersLike;

    private Set<Genres> genres;
    private Mpa mpa;
    private int rate;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", mpa.getId());
        values.put("film_rate", rate);
        return values;
    }
}
