package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mapper.UserMapper;
import ru.yandex.practicum.filmorate.dal.storage.FriendShipDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendShip;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.List;
import java.util.Optional;

@Slf4j
//@RequiredArgsConstructor
@Service
public class UserService {
    private final ValidationUser validation;
    private final UserDbStorage userDbStorage;
    private final FriendShipDbStorage friendShipDbStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserDbStorage userDbStorage, FriendShipDbStorage friendShipDbStorage) {
        this.userDbStorage = userDbStorage;
        this.friendShipDbStorage = friendShipDbStorage;
        validation = new ValidationUser();
    }

//    @Autowired
//    public UserService(@Qualifier("inMemoryUserStorage") UserStorage userStorage, @Qualifier("inMemoryFilmStorage") FilmStorage filmStorage) {
//        this.userStorage = userStorage;
//        this.filmStorage = filmStorage;
//        validation = new ValidationUser();
//    }

    public List<User> getUsers() {
        log.info("Значение{}", userDbStorage.allUsers());
        return userDbStorage.allUsers();
    }

    public User create(User user) {
        validation.validation(user);
        userDbStorage.save(user);
        log.info("Создан и сохранен новый пользователь и ему назначен id {}", user.getId());

        return user;
    }

    public User update(User user) {

        User oldUser = checkUsers(user.getId());

        Optional<User> user1 = userDbStorage.findByEmail(user.getEmail());
        if (user1.isPresent()) {
            log.error("Почта{} уже используется", user.getEmail());
            throw new ValidationException("Данная почта " + user.getEmail()
                    + " уже используется");
        }

        validation.validation(user);
        User newUser = UserMapper.updateUser(oldUser, user);

        log.info("Обновили данные о пользователе id {}", user.getId());
        return userDbStorage.update(newUser);
    }

    public User getUser(long id) {
        return checkUsers(id);
    }

    public User addFriends(Long user_id, Long friend_id) {
        User user = checkUsers(user_id);
        checkUsers(friend_id);

        Optional<FriendShip> friendShip = friendShipDbStorage.findFriendShip(user_id, friend_id);
        log.info("Проверяем друга{} ", friendShip);
        if (friendShip.isPresent()) {
            log.error("Пользователь: {} уже состоит в дружбе с пользователем: {}", friend_id, user_id);
            throw new ValidationException("Пользователь: " + friend_id
                    + " уже состоит в дружбе с пользователем: " + user_id);
        }

        FriendShip newFriendShip = new FriendShip(user_id, friend_id);
        friendShipDbStorage.save(newFriendShip);
        log.info("Пользователь {} добавлен в список друзей пользователя {}", friend_id, user_id);
        log.info("Друзья {}", newFriendShip);

        return user;
    }

    public User deleteFriend(Long user_id, Long friend_id) {
        User user = checkUsers(user_id);
        checkUsers(friend_id);
        friendShipDbStorage.delete(user_id, friend_id);
        log.info("Пользователи с id {} и {} теперь не являются друзьями", user_id, friend_id);
        return user;
    }

    public List<User> allFriends(Long id) {
        log.info("Используем allFriends ");
        checkUsers(id);
        log.info("Возвращаем список друзей пользователя с id:{}", id);
        log.info("Список друзей{}", friendShipDbStorage.getFriends(id));

        return friendShipDbStorage.getFriends(id);
    }

    public List<User> allMutualFriends(Long user_id1, Long user_id2) {
        log.info("Используем allMutualFriends ");
        checkUsers(user_id1);
        checkUsers(user_id2);

        return friendShipDbStorage.getMutualFriends(user_id1, user_id2);
    }

    private User checkUsers(Long user) {
        return userDbStorage.getUser(user).orElseThrow(() -> {
            log.error("Пользователь не найден: {}", user);
            return new UserNotExistException("Пользователь не найден: " + user);
        });
    }

}
