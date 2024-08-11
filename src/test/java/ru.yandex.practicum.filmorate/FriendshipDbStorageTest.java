package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.storage.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FriendshipDbStorage.class, UserDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FriendshipDbStorageTest {
    @Autowired
    private final FriendshipDbStorage friendshipDbStorage;
    @Autowired
    private final UserDbStorage userDbStorage;

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void saveFriendShipTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();

        final Friendship friendShip = new Friendship(user.getId(), user2.getId());

        friendshipDbStorage.save(friendShip);

        final Optional<Friendship> friendShip1 = friendshipDbStorage.findFriendship(user.getId(), user2.getId());

        assertTrue(friendShip1.isPresent());
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void deleteFriendShipTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();

        final Friendship friendShip = new Friendship(user.getId(), user2.getId());

        friendshipDbStorage.save(friendShip);

        final Optional<Friendship> friendShip1 = friendshipDbStorage.findFriendship(user.getId(), user2.getId());
        assertTrue(friendShip1.isPresent());

        friendshipDbStorage.delete(user.getId(), user2.getId());

        final Optional<Friendship> friendShip2 = friendshipDbStorage.findFriendship(user.getId(), user2.getId());
        assertTrue(friendShip2.isEmpty());
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getFriendsTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();
        final User user3 = userDbStorage.getUser(3L).get();

        final Friendship friendShip = new Friendship(user.getId(), user2.getId());
        final Friendship friendShip2 = new Friendship(user.getId(), user3.getId());

        friendshipDbStorage.save(friendShip);
        friendshipDbStorage.save(friendShip2);

        final List<User> users = friendshipDbStorage.getFriends(user.getId());

        assertEquals(users.get(0), user2);
        assertEquals(users.get(1), user3);
    }

    @Test
    @Sql(scripts = {"/clear.sql", "/create.sql"})
    void getMutualFriendsTest() {
        final User user = userDbStorage.getUser(1L).get();
        final User user2 = userDbStorage.getUser(2L).get();
        final User user3 = userDbStorage.getUser(3L).get();
        final User user4 = userDbStorage.getUser(4L).get();

        final Friendship friendShip = new Friendship(user.getId(), user2.getId());
        final Friendship friendShip2 = new Friendship(user.getId(), user3.getId());
        final Friendship friendShip3 = new Friendship(user4.getId(), user2.getId());
        final Friendship friendShip4 = new Friendship(user4.getId(), user3.getId());

        friendshipDbStorage.save(friendShip);
        friendshipDbStorage.save(friendShip2);
        friendshipDbStorage.save(friendShip3);
        friendshipDbStorage.save(friendShip4);

        final List<User> mutualFriends = friendshipDbStorage.getMutualFriends(user.getId(), user4.getId());

        assertEquals(mutualFriends.get(0), user2);
        assertEquals(mutualFriends.get(1), user3);
    }
}