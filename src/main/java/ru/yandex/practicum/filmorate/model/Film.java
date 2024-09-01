package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Film.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    //private final Set<Long> likes = new HashSet<>();

    public void setOneGenres(Genre genre) {
        genres.add(genre);
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hashDuration() {
        return !(duration == null || duration == 0);
    }

    public boolean hashReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hashMpa() {
        return (mpa != null);
    }

    public boolean hashGenres() {
        return !(genres == null || genres.isEmpty());
    }

}
