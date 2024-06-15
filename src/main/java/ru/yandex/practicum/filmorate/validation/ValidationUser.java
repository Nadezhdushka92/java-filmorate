package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class ValidationUser {
    public void validation(User user) {
        // проверяем выполнение необходимых условий
        String[] lineEmail = user.getEmail().split("");
        String email = "0";
        for (String string : lineEmail) {
            if (string.equals("@")) {
                email = "@";
            }
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || email.equals("0")) {
            log.info("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        // проверяем выполнение необходимых условий
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.info("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Login не может быть пустым");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Имя может быть пустым — в таком случае будет использован логин");
        }
    }
}
