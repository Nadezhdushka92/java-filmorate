package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;
    private final Map<Long, Film> films = new HashMap<>();

    public InMemoryFilmStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            return newFilm;
        } else {
            throw new FilmNotExistException("Неизвестный фильм, обновить невозможно ");
        }
    }

    @Override
    public Film findFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmNotExistException("Неизвестный фильм");
        }
    }

    @Override
    public void addLike(long filmId, long userId) {
        if (films.containsKey(filmId)) {
        Film film = findFilmById(filmId);
        if (film != null && userStorage.findUserById(userId) != null && findFilmById(filmId).getLikes() != null) {
            findFilmById(filmId).getLikes().add(userId);
        }
        } else {
            log.info("Фильм с id {} не существует", filmId);
            throw new FilmNotExistException("Неизвестный фильм");

        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        if (films.containsKey(filmId)) {
            Film film = findFilmById(filmId);
            if (film != null && userStorage.findUserById(userId) != null && film.getLikes() != null) {
                if (filmId > 0) {
                    film.getLikes().remove(userId);
                } else {
                    throw new UserNotExistException("Неизвестный пользователь");
                }
            }
        } else {
            throw new FilmNotExistException("Неизвестный фильм, невозможно удалить лайк");
        }
    }
}
