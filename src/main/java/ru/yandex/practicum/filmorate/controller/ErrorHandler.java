package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ValidationException.class, MethodArgumentNotValidException.class})
    public ErrorResponse handleValidationException(final ValidationException exception) {
        return new ErrorResponse("Ошибка валидации", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundFilm(final FilmNotExistException exception) {
        return new ErrorResponse("Film объект не найден", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundUser(final UserNotExistException exception) {
        return new ErrorResponse("User объект не найден", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundGenre(final GenreNotExistException exception) {
        return new ErrorResponse("Genre объект не найден", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundMpa(final MpaNotExistException exception) {
        return new ErrorResponse("Mpa объект не найден", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class, InternalServerException.class})
    public ErrorResponse handleInternalServerErrorException(final InternalServerException exception) {
        return new ErrorResponse("Возникло исключение.", exception.getMessage());
    }
}