package ru.yandex.practicum.filmorate.exception;

public class MpaNotExistException extends RuntimeException {
    public MpaNotExistException(String message) {
        super(message);
    }
}