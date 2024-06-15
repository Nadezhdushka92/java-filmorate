package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updateFilm) {
        return filmService.update(updateFilm);
    }

    @GetMapping("/{filmId}")
    public Film findFilmById(@PathVariable long filmId) {
        return filmService.findFilmById(filmId);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}
