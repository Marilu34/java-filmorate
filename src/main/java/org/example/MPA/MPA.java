package org.example.MPA;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MPA {
    private Integer id;
    private String name;
}

