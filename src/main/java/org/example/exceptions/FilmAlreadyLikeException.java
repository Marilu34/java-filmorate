package org.example.exceptions;

public class FilmAlreadyLikeException extends RuntimeException {

    public FilmAlreadyLikeException(String message) {
        super(message);
    }
}
