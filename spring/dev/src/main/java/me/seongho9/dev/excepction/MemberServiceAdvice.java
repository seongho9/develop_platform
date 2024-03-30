package me.seongho9.dev.excepction;

import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.excepction.member.MemberExistException;
import me.seongho9.dev.excepction.member.MemberNotFoundException;
import me.seongho9.dev.excepction.member.PasswordNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MemberServiceAdvice {

    @ExceptionHandler(value = {MemberNotFoundException.class})
    public ResponseEntity<ErrorResponse> memberNotFoundExceptionHandler(Exception ex) {

        log.error("[ERROR][MEMBER] {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(404, ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {PasswordNotMatchException.class})
    public ResponseEntity<ErrorResponse> memberPasswordNotMatchExceptionHandler(Exception ex) {

        log.error("[ERROR][MEMBER] {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(400, ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MemberExistException.class})
    public ResponseEntity<ErrorResponse> memberExistExceptionHandler(Exception ex) {

        log.error("[ERROR][MEMBER] {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(409, ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
