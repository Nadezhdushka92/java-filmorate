package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController ( UserService userService ) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User create( @Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        return userService.update(updateUser);
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable long userId){
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
    public void removeFromFriends ( @PathVariable long userId, @PathVariable long friendId ) {
        userService.removeFromFriends(userId, friendId);
    }

    @GetMapping(value = ("/users/{userId}/friends/common/{otherId}"))
    public List<User> getCommonFriends( @PathVariable long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId,otherId);
    }

    @GetMapping(value = ("/users/{userId}"))
    public List<User> getCommonAllFriends( @PathVariable long userId) {
        return userService.getAllFriends(userId);
    }

    }
