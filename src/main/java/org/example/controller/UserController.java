package org.example.controller;


import lombok.Data;
import org.example.exceptions.IDException;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ValidationException;
import org.example.model.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

@Data
@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;//не може начинаться с нуля

    private int generateId() {
        return id++;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (users.containsValue(user)) {
            users.put(user.getId(), user);
            log.info("Пользователь с id = {} обновлен", user.getId());
        }
        user.setId(generateId());
        validate(user);
        users.put(user.getId(), user);
        log.info("Создан User с id:{}", user.getId());
        return user;
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            //     log.debug("У User с id:{} нет имени", user.getId());
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым");
//            user.setLogin(user.getName().trim());
//            log.debug("У User с id:{} нет логина", user.getId());
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может содержать пробелы");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(user.getEmail());
            log.debug("У User с id:{} нет почты", user.getId());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения должна быть не будущей");
//            user.setBirthday(LocalDate.now());
//            log.debug("У User с id:{} нет почты", user.getId());
        }
    }

    @GetMapping
    public Collection<User> getAll() {

        return users.values();
    }

    @PutMapping
    public User put(@RequestBody @Valid User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("User не найден");
            //   log.debug("User с id:{} не найден", user.getId());
        } else {
            validate(user);
            users.put(user.getId(), user);
            log.info("User с id:{} update", user.getId());
        }
        return user;
    }
}