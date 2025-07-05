package com.example.ticketmaster.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Retorna 400 Bad Request por padrão
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}