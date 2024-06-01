package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        // проверяем выполнение необходимых условий
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new ValidationException("Описание не может быть пустым");
        }
        // формируем дополнительные данные
        film.setId(getNextId());
        log.debug("Создан и назначен id {}", film.getId());
        // сохраняем новый фильм в памяти приложения
        films.put(film.getId(), film);

        return film;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updateFilm) {
        // проверяем необходимые условия
        if (updateFilm.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(updateFilm.getId())) {
            Film updatedFilm = films.get(updateFilm.getId());
            if (updateFilm.getDescription() == null || updateFilm.getDescription().isBlank()) {
                throw new ValidationException("Описание не может быть пустым");
            } else {
                // если фильм найден и все условия соблюдены, обновляем его содержимое
                updatedFilm.setDescription(updateFilm.getDescription());
                log.info("Изменили описание фильма");
            }
            if (updateFilm.getName() == null || updateFilm.getName().isBlank()) {
                throw new ValidationException("Имя не может быть пустым");
            } else {
                // если фильм найден и все условия соблюдены, обновляем его содержимое
                updatedFilm.setName(updateFilm.getName());
                log.info("Изменили название фильма");
            }
            if (updateFilm.getReleaseDate() == null) {
                throw new ValidationException("Дата релиза не может быть пустой");
            } else {
                // если фильм найден и все условия соблюдены, обновляем его содержимое
                updatedFilm.setReleaseDate(updateFilm.getReleaseDate());
                log.info("Изменили дату релиза фильма");
            }
            if (updateFilm.getDuration() == null) {
                throw new ValidationException("Продолжительность не может быть пустой");
            } else {
                // если фильм найден и все условия соблюдены, обновляем его содержимое
                updatedFilm.setDuration(updateFilm.getDuration());
                log.info("Изменили продолжительность фильма");
            }
            return updatedFilm;
        }
        log.error("Не найден фильм с id {}", updateFilm.getId());
        throw new ValidationException("Фильм с id = " + updateFilm.getId() + " не найден");
    }
}
