package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final ValidationFilm validation;
    public final FilmStorage filmStorage;
    protected int idCnt = 0;

    @Autowired
    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        validation = new ValidationFilm();
    }

    public Collection<Film> findAll() {
        return filmStorage.getFilms().values();
    }

    public Film create(Film film) {
        validation.validation(film);
        film.setId(getNextId());
        log.debug("Добавим новый фильм и ему назначим id {}", film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validation.validation(film);
        if (filmStorage.findFilmById(film.getId()) == null) {
            log.warn("Невозможно обновить фильм");
            throw new FilmNotExistException("Невозможно обновить фильм");
        }
            log.info("Фильм с id {} обновлён", film.getId());
            return filmStorage.update(film);
    }

    public Film findFilmById(long filmId) {
        Film film = filmStorage.findFilmById(filmId);
        if (film == null) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new FilmNotExistException("Фильм с id не найден");
        }
        return film;
    }

    public void addLike(long id, long userId) {
        filmStorage.addLike(id, userId);
        log.info("Пользователь с id {} добавил лайк фильму с id {} ", id, userId);
    }

    public void deleteLike(long id, long userId) {
        filmStorage.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил лайк с id {}", userId, id);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted((a1, a2) -> a2.getLikes().size() - a1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        idCnt++;
        return idCnt;
    }
}
