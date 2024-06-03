package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ReleaseUserValidator;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    protected int idCnt = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Login не может быть пустым");
        }
        if (user.getName() == null) {
            log.debug("Имя не может быть пустым {}", user.getId());
            user.setName("common");
            //throw new ValidationException("Имя не может быть пустым");
        }
        // формируем дополнительные данные
        user.setId(getNextId());
        log.debug("Создан новый пользователь и ему назначен id {}", user.getId());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        // проверяем необходимые условия
        if (updateUser.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(updateUser.getId())) {
            User updatedUser = users.get(updateUser.getId());
                if (userValidation(updatedUser)) {

                    // если пользователь найден и все условия соблюдены, обновляем его содержимое
                    updatedUser.setEmail(updateUser.getEmail());
                    log.info("Изменили email пользователя");

                    // если фильм найден и все условия соблюдены, обновляем его содержимое
                    updatedUser.setName(updateUser.getLogin());
                    log.info("Изменили login пользователя");

                    // если фильм найден и все условия соблюдены, обновляем его содержимое
                    updatedUser.setName(updateUser.getName());
                    log.info("Изменили имя пользователя");

                    // если фильм найден и все условия соблюдены, обновляем его содержимое
                    updatedUser.setBirthday(updateUser.getBirthday());
                    log.info("Изменили дату рождения пользователя");
                }
            return updateUser;
        }
        log.error("Не найден пользователь с id {}", updateUser.getId());
        throw new ValidationException("Пользователь с id = " + updateUser.getId() + " не найден");
    }

    public boolean userValidation(User user) {

        if (user.getEmail() != null && (user.getLogin() != null
             && !user.getLogin().isBlank())) {
            return true;
        } else {
            throw new ValidationException("Валидация данных пользователя не пройдена");
        }

    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        idCnt++;
        return idCnt;
    }
}
