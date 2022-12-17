package com.bastiansmn.vp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApiError {
    private Date timestamp;
    private String message;
    private String devMessage;
    private HttpStatus httpStatusString;
    private Integer httpStatus;
}