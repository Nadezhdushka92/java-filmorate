package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        return userService.update(updateUser);
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable long userId) {
        return userService.findUserById(userId);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }


    @PutMapping(value = "/{userId}/friends/{friendId}")
        public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
            userService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/{userId}/friends/{friendId}")
    public void removeFromFriends(@PathVariable long userId, @PathVariable long friendId) {
        userService.removeFromFriends(userId, friendId);
    }

    @GetMapping(value = ("/{userId}/friends/common/{otherId}"))
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId,otherId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getCommonAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }
    }

