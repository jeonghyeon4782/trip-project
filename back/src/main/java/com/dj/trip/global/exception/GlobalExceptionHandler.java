package com.dj.trip.global.exception;

import com.dj.trip.global.dto.ResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    // MethodArgumentNotValidException 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("----------------------------- 유효성 검사 에러 발생-----------------------------");
        ex.printStackTrace();
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getFieldError().getDefaultMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    // Exception 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> exceptionHandler(Exception ex) {
        log.error("-----------------------------에러 발생-----------------------------");
        ex.printStackTrace();
        ResponseDto<String> responseDto = new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), "에러 발생", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}
