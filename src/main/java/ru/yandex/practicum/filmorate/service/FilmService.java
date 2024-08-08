package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dal.storage.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
//@RequiredArgsConstructor
@Service
public class FilmService {
    private final ValidationFilm validation;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmLikeDbStorage likeDbStorage;
    private final FilmMpaDbStorage mpaDbStorage;
    private final FilmGenreDbStorage genreDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmDbStorage filmDbStorage, @Qualifier("userDbStorage") UserDbStorage userDbStorage, FilmLikeDbStorage likeDbStorage, FilmMpaDbStorage mpaDbStorage, FilmGenreDbStorage genreDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
        this.likeDbStorage = likeDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
        validation = new ValidationFilm();
    }

//    @Autowired
//    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage) {
//        this.filmStorage = filmStorage;
//        validation = new ValidationFilm();
//    }


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

    public Film addLike(Long film_id, Long user_id) {
        Film film = checkFilm(film_id);
        userDbStorage.getUser(user_id).orElseThrow(() -> {
            log.error("Пользователь не найден{}", user_id);
            return new UserNotExistException("Пользователь  не найден " + user_id);
        });

        Optional<Like> like = likeDbStorage.findLike(film_id, user_id);
        if (like.isPresent()) {
            log.error("Пользователь уже поставил лайк{} на фильм {}", user_id, film_id);
            throw new ValidationException("Пользователь уже поставил лайк " +
                    user_id + " на фильм " + film_id);
        }
        likeDbStorage.save(new Like(film_id, user_id));
        log.info("Пользователем с id {} добавлен лайк фильму {}", user_id, film_id);
        return film;
    }

    public Film deleteLike(Long film_id, Long user_id) {
        log.info("Запуск метода deleteLike");
        Film film = checkFilm(film_id);
        userDbStorage.getUser(user_id).orElseThrow(() -> {
            log.error("Пользователь не найден{}", user_id);
            return new UserNotExistException("Пользователь  не найден " + user_id);
        });
        if (!likeDbStorage.delete(film_id, user_id)) {
            log.error("Лайк пользователя с id {} у фильма {} не удален", user_id, film_id);
            throw new ValidationException("Лайк " + user_id + " у фильма " +
                    film + " не удален");
        }
        log.info("Пользователь с id {} удалил лайк с id {}", user_id, film_id);
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

