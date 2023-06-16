package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genres {
    private int id;
    private String name;
}
