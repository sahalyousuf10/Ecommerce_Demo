package com.example.sahal.SellerService.Exception;

import com.example.sahal.SellerService.Dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        protected Map<String, String> handleMethodArgumentNotValid(
                MethodArgumentNotValidException ex, WebRequest request) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult()
                    .getFieldErrors()
                    .forEach(x -> {
                        errors.put(x.getField(),
                                x.getDefaultMessage());}
                    );
            return errors;
        }

        // handle global exceptions
        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorDto handleGlobalException
        (Exception exception){
            ErrorDto errorDto = new ErrorDto(
                    "Exception",
                    exception.getMessage(),
                    new Date());
            return errorDto;
        }
}
