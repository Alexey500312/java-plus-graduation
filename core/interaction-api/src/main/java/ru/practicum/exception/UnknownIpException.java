package ru.practicum.exception;

public class UnknownIpException extends RuntimeException {
    public UnknownIpException(String message) {
        super(message);
    }
}
