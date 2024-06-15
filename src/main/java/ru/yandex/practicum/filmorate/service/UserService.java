package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final ValidationUser validation;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    protected int idCnt = 0;

    @Autowired
    public UserService ( @Qualifier("inMemoryUserStorage") UserStorage userStorage, @Qualifier("inMemoryFilmStorage") FilmStorage filmStorage ) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        validation = new ValidationUser();
    }

    public Collection<User> findAll() {
        return userStorage.findAll().values();
    }

    public User create(User user) {
        user.setId(getNextId());
        validation.validation(user);
        log.debug("Добавлен новый пользователь и ему назначен id {}", user.getId());
        return userStorage.create(user);
    }

    public User update(User user) {
        validation.validation(user);
        log.info("Пользователь с id {} будет обновлён",user.getId());
        return userStorage.update(user);
    }

    public User findUserById(long userId) {
        User user = userStorage.findUserById(userId);
        if (user == null) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new UserNotExistException("Пользователь с id не найден");
        }
        return user;
    }

    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
        log.info("Пользователь с id {} удалён", userId);
    }

    public void addFriend(long userId, long friendId){
        userStorage.addFriend(userId, friendId);
        log.info("Пользователи с id {} и {} теперь друзья", userId, friendId);
    }

    public void removeFromFriends(long userId, long friendId) {
        userStorage.removeFromFriends(userId, friendId);
        log.info("Пользователи с id {} и {} теперь не являются друзьями", userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public List<User> getAllFriends(long userId) {
        return userStorage.getAllFriends(userId);
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        idCnt++;
        return idCnt;
    }
}
