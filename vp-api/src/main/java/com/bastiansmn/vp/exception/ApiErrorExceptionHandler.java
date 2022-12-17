package com.bastiansmn.vp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class ApiErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FunctionalException.class)
    public final ResponseEntity<ApiError> handleFunctionalError(FunctionalException ex) {
        ApiError apiError = new ApiError(
                new Date(),
                ex.getClientMessage(),
                ex.toString(),
                ex.getHttpStatus(),
                ex.getHttpStatus().value()
        );
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(apiError);
    }

    @ExceptionHandler(TechnicalException.class)
    public final ResponseEntity<ApiError> handleFunctionalError(TechnicalException ex) {
        ApiError apiError = new ApiError(
                new Date(),
                ex.getClientMessage(),
                ex.toString(),
                ex.getHttpStatus(),
                ex.getHttpStatus().value()
        );
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(apiError);
    }

}
