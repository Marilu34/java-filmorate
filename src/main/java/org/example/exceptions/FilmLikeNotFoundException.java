package org.example.exceptions;

public class FilmLikeNotFoundException extends RuntimeException{

    public FilmLikeNotFoundException(String message) {
        super(message);
    }
}
