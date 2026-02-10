package com.napptilus.challenge.infrastructure.http.controller;

import com.napptilus.challenge.domain.exception.ProductPriceNotFoundException;
import com.napptilus.challenge.infrastructure.http.controller.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import static java.util.Objects.nonNull;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    private static final String UNEXPECTED_ERROR_CODE = "ERR_PRODUCT_PRICE_UNEXPECTED";
    private static final String MISSING_APPLICATION_DATE_ERROR_CODE = "ERR_PRODUCT_PRICE_MISSING_APPLICATION_DATE";
    private static final String INVALID_PARAM_ERROR_CODE = "ERR_PRODUCT_PRICE_INVALID_PARAM";
    private static final String RESOURCE_NOT_FOUND_ERROR_CODE = "ERR_PRODUCT_PRICE_RESOURCE_NOT_FOUND";

    @ExceptionHandler(ProductPriceNotFoundException.class)
    public ResponseEntity<Void> handleProductPriceNotFoundException(ProductPriceNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestValueException(MissingRequestValueException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .code(MISSING_APPLICATION_DATE_ERROR_CODE)
                        .message(ex.getReason())
                        .build()
                );
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleServerWebInputException(ServerWebInputException ex) {
        if (ex.getCause() instanceof TypeMismatchException typeMismatchException) {
            if (nonNull(typeMismatchException.getPropertyName())) {
                String supportedValues = switch (typeMismatchException.getPropertyName()) {
                    case "application_date" -> "Only dates in the format YYYY-MM-DDTHH:MM:SS are supported.";
                    case "brand_id", "product_id" -> "Only whole numbers are supported.";
                    default -> "";
                };

                return ResponseEntity.badRequest()
                        .body(ErrorResponse.builder()
                                .code(INVALID_PARAM_ERROR_CODE)
                                .message("The value %s of the parameter %s is not valid. %s".formatted(typeMismatchException.getValue(), typeMismatchException.getPropertyName(), supportedValues))
                                .build()
                        );
            }
        }

        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .code(INVALID_PARAM_ERROR_CODE)
                        .message("Some parameter is not valid. Please, check the request URL.")
                        .build()
                );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .code(RESOURCE_NOT_FOUND_ERROR_CODE)
                        .message("The resource you are trying to retrieve does not exist. Please, check the URL.")
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.builder()
                        .code(UNEXPECTED_ERROR_CODE)
                        .message("There was an unexpected error.")
                        .build()
                );
    }
}
