package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("inMemoryFilmStorage")
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
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film findFilmById(Long id) {
        return null;
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = findFilmById(filmId);
        if ((film != null) && (userStorage.findUserById(userId) != null)) {
            film.getLikes().add(userId);
        } else {
            log.info("Пользователю с id {} не удалось поставить фильму с id {} лайк", userId, filmId);
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        Film film = findFilmById(filmId);
        if ((film != null) && (userStorage.findUserById(userId) != null)) {
            film.getLikes().remove(userId);
        }
    }
}
