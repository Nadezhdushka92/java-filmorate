package ru.yandex.practicum.filmorate.dal.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    List<User> allUsers();

    Optional<User> getUser(Long id);

    User update(User user);

    User save(User user);

//    User create(User user);
//
//    User update(User user);
//
//    Map<Long, User> findAll();
//
//    User findUserById(long userId);
//
//    void deleteUser(long userId);
//
//    void addFriend(long userId, long friendId);
//
//    void removeFromFriends(long userId, long friendId);
//
//    List<User> getCommonFriends(long userId, long otherId);
//
//    List<User> getAllFriends(long userId);
}