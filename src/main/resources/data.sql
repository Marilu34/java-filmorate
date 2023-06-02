INSERT INTO MPA (MPA_name) SELECT 'G'
WHERE NOT EXISTS (SELECT MPA_name FROM MPA WHERE MPA_name = 'G');

INSERT INTO MPA (MPA_name) SELECT 'PG'
WHERE NOT EXISTS (SELECT MPA_name FROM MPA WHERE MPA_name = 'PG');

INSERT INTO MPA (MPA_name) SELECT 'PG-13'
WHERE NOT EXISTS (SELECT MPA_name FROM MPA WHERE MPA_name = 'PG-13');

INSERT INTO MPA (MPA_name) SELECT 'R'
WHERE NOT EXISTS (SELECT MPA_name FROM MPA WHERE MPA_name = 'R');

INSERT INTO MPA (MPA_name) SELECT 'NC-17'
WHERE NOT EXISTS (SELECT MPA_name FROM MPA WHERE MPA_name = 'NC-17');

INSERT INTO genre (genre_id, genre_name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Фантастика'),
           (5, 'Триллер'),
           (6, 'Боевик');