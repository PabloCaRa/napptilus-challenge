package com.napptilus.challenge.infrastructure.persistence.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InfrastructureException extends RuntimeException {
    public InfrastructureException(String message, Throwable cause) {
        super(message);
        log.error(message, cause);
    }
}
