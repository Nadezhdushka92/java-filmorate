package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dal.storage.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final ValidationFilm validation;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmLikeDbStorage likeDbStorage;
    private final FilmMpaDbStorage mpaDbStorage;
    private final FilmGenreDbStorage genreDbStorage;

    public List<Film> getFilms() {
        return filmDbStorage.findAllFilms();
    }

    public Film create(Film film) {
        Optional<Film> newFilm = filmDbStorage.getFilm(film.getId());
        if (newFilm.isPresent()) {
            log.error("Фильм {} уже существует", film.getId());
            throw new ValidationException("Фильм уже существует");
        }
        validation.validation(film);
        filmDbStorage.save(film);

        if (film.hashMpa()) {
            if ((film.getMpa().getId() != null) || film.getMpa().getName() != null) {
                Mpa mpa = mpaDbStorage.getMpa(film.getMpa().getId()).orElseThrow(() ->
                        new ValidationException("Такого mpa нет"));
                mpaDbStorage.updateMpa(mpa, film.getId());
            }
        }

        if (film.hashGenres()) {
            if ((film.getGenres().getLast().getId() != null) || (film.getGenres().getLast().getName() != null)) {
                if (film.getGenres().stream().allMatch(genre ->
                        genreDbStorage.getGenre(genre.getId()).isEmpty())) {
                    throw new ValidationException("Такого жанра нет");

                } else {
                    genreDbStorage.findAllGenres().forEach(genre ->
                            film.getGenres().stream()
                                    .filter(filmGenre -> Objects.equals(filmGenre.getId(), genre.getId()))
                                    .findFirst()
                                    .ifPresent(filmGenre -> genreDbStorage.save(film, genre))
                    );
                }
            }
        }
        log.info("Фильм {} создан", film);
        return film;
    }

    public Film update(Film film) {
        Optional<Mpa> mpa = mpaDbStorage.getMpa(film.getMpa().getId());
        mpa.ifPresent(film::setMpa);

        log.info("Mpa фильма{}", film.getMpa());
        Film oldFilm = checkFilm(film.getId());
        validation.validation(film);
        Film newFilm = FilmMapper.updateFilm(oldFilm, film);
        log.info("Обновили данные о фильме id {}", film.getId());
        return filmDbStorage.update(newFilm);
    }

    public Film getFilm(Long id) {
        Film film = checkFilm(id);

        List<Genre> genre = genreDbStorage.getGenresByIdFilm(film.getId());
        if (genre != null && !genre.isEmpty()) {
            film.setGenres(genre);
        }
        Long idMpa = mpaDbStorage.getMpaId(film.getId());
        Optional<Mpa> mpa = mpaDbStorage.getMpa(idMpa);
        mpa.ifPresent(film::setMpa);

        log.info("Возвращаем фильм {}", film);
        return film;
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = checkFilm(filmId);
        userDbStorage.getUser(userId).orElseThrow(() -> {
            log.error("Пользователь не найден{}", userId);
            return new UserNotExistException("Пользователь  не найден " + userId);
        });

        Optional<Like> like = likeDbStorage.findLike(filmId, userId);
        if (like.isPresent()) {
            log.error("Пользователь уже поставил лайк{} на фильм {}", userId, filmId);
            throw new ValidationException("Пользователь уже поставил лайк " +
                    userId + " на фильм " + filmId);
        }
        likeDbStorage.save(new Like(filmId, userId));
        log.info("Пользователем с id {} добавлен лайк фильму {}", userId, filmId);
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        log.info("Запуск метода deleteLike");
        Film film = checkFilm(filmId);
        userDbStorage.getUser(userId).orElseThrow(() -> {
            log.error("Пользователь не найден{}", userId);
            return new UserNotExistException("Пользователь  не найден " + userId);
        });
        if (!likeDbStorage.delete(filmId, userId)) {
            log.error("Лайк пользователя с id {} у фильма {} не удален", userId, filmId);
            throw new ValidationException("Лайк " + userId + " у фильма " +
                    film + " не удален");
        }
        log.info("Пользователь с id {} удалил лайк с id {}", userId, filmId);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return likeDbStorage.getPopularFilms(count);
    }

    private Film checkFilm(Long id) {
        if (id == null) {
            log.error("Фильм с id{} не найден", id);
            throw new FilmNotExistException("Фильм с id: " + id + " не найден.");
        }
        return filmDbStorage.getFilm(id).orElseThrow(() -> {
            log.error("Фильм с id{} не найден", id);
            return new FilmNotExistException("Фильм с id: " + id + " не найден.");
        });
    }

    //===================================MPA=====================================
    public List<Mpa> getAllMpa() {
        return mpaDbStorage.findAllMpa();
    }

    public Mpa getMpa(Long id) {
        return mpaDbStorage.getMpa(id).orElseThrow(() -> {
            log.error("Mpa{} не найден", id);
            return new MpaNotExistException("Mpa " + id + " не найден");
        });
    }

    public List<Genre> getAllGenre() {
        return genreDbStorage.findAllGenres();
    }

    public Genre getGenre(Long id) {
        return genreDbStorage.getGenre(id).orElseThrow(() -> {
            log.error("Genre{} не найден", id);
            return new GenreNotExistException("Genre " + id + " не найден");
        });
    }

}

