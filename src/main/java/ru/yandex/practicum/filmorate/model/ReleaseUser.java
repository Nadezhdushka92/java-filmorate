package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ReleaseUserValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ReleaseUser {
    String message () default "Дата рождения не может быть из будущего";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload () default {};
}
