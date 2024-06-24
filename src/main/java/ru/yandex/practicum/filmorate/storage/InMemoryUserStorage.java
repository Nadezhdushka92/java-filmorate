package ru.yandex.practicum.filmorate.storage;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        //сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            return newUser;
        } else {
            throw new UserNotExistException("Неизвестный пользователь");
        }
    }

    @Override
    public Map<Long, User> findAll() {
        return users;
    }

    @Override
    public User findUserById(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new UserNotExistException("Ошибка неизвестный id пользователя");
        }
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        if (users.containsKey(userId) && users.containsKey(friendId)) {
            User user = findUserById(userId);
            User friend = findUserById(friendId);
            if (user != null && friend != null) {
                friend.getFriends().add(userId);
                user.getFriends().add(friendId);
            }
        } else {
            throw new UserNotExistException("Ошибка неизвестный id пользователя");
        }
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        if (users.containsKey(userId) && users.containsKey(friendId)) {
            User user = findUserById(userId);
            User friend = findUserById(friendId);
            if (user != null && friend != null && user.getFriends() != null) {
                user.getFriends().remove(friendId);
                friend.getFriends().remove(userId);
            }
        } else {
            throw new UserNotExistException("Ошибка удаления неизвестного id пользователя или друга");
        }
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        List<User> commonFriends = new ArrayList<>();
        User user = findUserById(userId);
        User otherUser = findUserById(otherId);
        if (user != null && otherUser != null && !otherUser.getFriends().isEmpty() && !user.getFriends().isEmpty()) {
            Set<Long> commonFriendsIds = Sets.intersection(user.getFriends(),otherUser.getFriends());
            for (Long friendsId : commonFriendsIds) {
                commonFriends.add(findUserById(friendsId));
            }
            return commonFriends;
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public List<User> getAllFriends(long userId) {
        List<User> friends = new ArrayList<>();
        User user = findUserById(userId);
        if (user != null && !user.getFriends().isEmpty()) {
            for (Long friendIds : user.getFriends()) {
                friends.add(findUserById(friendIds));
            }
            return friends;
        } else {
            return new ArrayList<>();
        }
    }
}
