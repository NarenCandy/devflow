package com.devflow.devflow.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devflow.devflow.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorResponseDTO> handleRunTimeException(RuntimeException ex) {
            ErrorResponseDTO er = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
        }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoSuchElementException(NoSuchElementException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    


}
