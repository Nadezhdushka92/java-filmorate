package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Long id;
    private String name;
    private String description;
    private String releaseDate;
    private Integer duration;
    private final Set<Long> likes = new HashSet<>();

}
