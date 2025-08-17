package ru.practicum.exception;

public class UpdateEntityException extends RuntimeException {
    public UpdateEntityException(String message) {
        super(message);
    }
}
