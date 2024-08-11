package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ValidationUser {
    public void validation(User user) {
        // проверяем выполнение необходимых условий
        String[] lineEmail = user.getEmail().split("");
        String email = "-1";
        for (String string : lineEmail) {
            if (string.equals("@")) {
                email = "@";
            }
        }

        if (user.getEmail().isEmpty() || email.equals("-1")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        String[] lineLogin = user.getLogin().split("");
        String login = "1";
        for (String string : lineLogin) {
            if (string.equals(" ")) {
                login = "-1";
            }
        }

        // проверяем выполнение необходимых условий
        if (user.getLogin().isEmpty() || login.equals("-1")) {
            log.info("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Login не может быть пустым");
        }

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Имя может быть пустым — в таком случае будет использован логин");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nowDate = LocalDate.now();
        LocalDate birthday = LocalDate.parse(user.getBirthday().toString(), formatter);

        if (nowDate.isBefore(birthday)) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
