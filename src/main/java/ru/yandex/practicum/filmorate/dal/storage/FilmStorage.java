package ru.yandex.practicum.filmorate.dal.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    Film save(Film film);

    Film update(Film film);

    List<Film> findAllFilms();

    Optional<Film> getFilm(Long id);
//    Map<Long, Film> getFilms();
//
//    Film create(Film film);
//
//    Film update(Film film);
//
//    Film findFilmById(Long id);
//
//    void addLike(long filmId, long userId);
//
//    void deleteLike(long filmId, long userId);
}