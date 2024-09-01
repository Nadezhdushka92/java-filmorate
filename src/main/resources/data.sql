--INSERT INTO users (email, login, name, birthday)
--  VALUES ('nady@yandex.ru', 'Nady92', 'Nadya', '2007-12-03');
--SELECT * FROM USERS;
--
UPDATE films
SET mpa_id = NULL;

DELETE FROM mpa;
DELETE FROM films_genres;
DELETE FROM genres;

MERGE INTO genres (id, name)
    KEY(id)
    values (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO mpa (id, name)
    KEY(id)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');