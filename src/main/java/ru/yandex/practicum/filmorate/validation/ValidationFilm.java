package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ValidationFilm {

    public void validation(Film film) {
        if (film.getName().isEmpty())  {
            throw new ValidationException("Название не может быть пустым;");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data1 = LocalDate.parse("1895-12-28", formatter);
        LocalDate data2 = LocalDate.parse(film.getReleaseDate(), formatter);

        if (data2.isBefore(data1)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}