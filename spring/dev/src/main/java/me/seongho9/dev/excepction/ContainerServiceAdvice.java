package me.seongho9.dev.excepction;

import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.excepction.container.ContainerConflictException;
import me.seongho9.dev.excepction.container.ContainerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class ContainerServiceAdvice {

    @ExceptionHandler(value = {ContainerNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleContainerNotFound(Exception ex) {

        log.error("[ERROR][CONTAINER] {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(404, ex.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ContainerConflictException.class})
    public ResponseEntity<ErrorResponse> handleContainerDuplicate(Exception ex) {

        log.error("[ERROR][CONTAINER] {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(409, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
