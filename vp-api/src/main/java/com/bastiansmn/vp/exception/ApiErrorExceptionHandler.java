package com.bastiansmn.vp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
class ApiError {
    private Date timestamp;
    private String message;
    private String devMessage;
    private HttpStatus httpStatusString;
    private Integer httpStatus;
}

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
