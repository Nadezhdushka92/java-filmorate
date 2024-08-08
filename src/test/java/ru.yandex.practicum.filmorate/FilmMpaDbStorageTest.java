package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.FilmMpaDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmMpaDbStorage.class, FilmDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FilmMpaDbStorageTest {
    @Autowired
    private final FilmDbStorage filmDbStorage;
    @Autowired
    private final FilmMpaDbStorage filmMpaDbStorage;

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getMpaTest() {
        Mpa mpa = filmMpaDbStorage.getMpa(1L).get();

        assertThat(mpa).hasFieldOrPropertyWithValue("mpa_id", 1L);
        assertThat(mpa).hasFieldOrPropertyWithValue("mpa_name", "G");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void valueMpaTest() {
        List<Mpa> mpa = filmMpaDbStorage.findAllMpa();

        assertThat(mpa).hasSize(5);
        assertThat(mpa.get(0)).hasFieldOrPropertyWithValue("mpa_id", 1L);
        assertThat(mpa.get(0)).hasFieldOrPropertyWithValue("mpa_name", "G");

        assertThat(mpa.get(1)).hasFieldOrPropertyWithValue("mpa_id", 2L);
        assertThat(mpa.get(1)).hasFieldOrPropertyWithValue("mpa_name", "PG");
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void updateMpaTest() {
        final Film film = filmDbStorage.getFilm(1L).get();

        assertThat(film).hasFieldOrPropertyWithValue("mpa_name", null);

        final Mpa mpa = filmMpaDbStorage.getMpa(1L).get();

        filmMpaDbStorage.updateMpa(mpa, film.getFilm_id());
        Long id = filmMpaDbStorage.getMpaId(film.getFilm_id());
        film.setMpa(filmMpaDbStorage.getMpa(id).get());

        assertThat(film).hasFieldOrPropertyWithValue("mpa_name", mpa);
    }
}