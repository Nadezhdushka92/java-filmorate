package ru.yandex.practicum.filmorate.storage;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create ( User user ) {
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update ( User newUser ) {
            users.put(newUser.getId(), newUser);
            return newUser;
    }

    @Override
    public Map<Long, User> findAll () {
        return users;
    }

    @Override
    public User findUserById ( long userId ) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        return null;
    }

    @Override
    public void deleteUser ( long userId ) {
        users.remove(userId);
    }

    @Override
    public void addFriend ( long userId, long friendId ) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            user.getFriends().add(userId);
        }
    }

    @Override
    public void removeFromFriends ( long userId, long friendId ) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            user.getFriends().remove(userId);
        }
    }

    @Override
    public List<User> getCommonFriends ( long userId, long otherId ) {
        List<User> commonFriends = new ArrayList<>();
        User user = findUserById(userId);
        User otherUser = findUserById(otherId);
        if (user != null && otherUser != null) {
            Set<Long> commonFriendsIds = Sets.intersection(user.getFriends(),otherUser.getFriends());
            for (Long friendsId : commonFriendsIds) {
                commonFriends.add(findUserById(friendsId));
            }
        }
        return commonFriends;
    }

    @Override
    public List<User> getAllFriends ( long userId ) {
        List<User> friends = new ArrayList<>();
        User user = findUserById(userId);
        if (user != null) {
            for (Long friendIds : user.getFriends()) {
                friends.add(findUserById(friendIds));
            }
        }
        return friends;
    }
}
