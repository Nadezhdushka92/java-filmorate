package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ReleaseUserValidator implements ConstraintValidator<ReleaseUser, LocalDate> {
    private static final LocalDate DATE_OF_RELEASE = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(DATE_OF_RELEASE);
    }

    @Override
    public void initialize(ReleaseUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
