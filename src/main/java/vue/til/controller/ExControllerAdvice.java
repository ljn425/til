package vue.til.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vue.til.dto.ErrorResponseDto;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponseDto illegalExHandel(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        return new ErrorResponseDto(400, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponseDto exHandle(Exception e) {
        log.error("Exception", e);
        return new ErrorResponseDto(500, e.getMessage());
    }
}
