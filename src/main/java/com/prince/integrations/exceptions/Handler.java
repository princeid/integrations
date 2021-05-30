package com.prince.integrations.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RestController
public class Handler implements ErrorController {
    private static Logger logger = LoggerFactory.getLogger(Handler.class);

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage(), e.getErrors()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleError(MissingRequestHeaderException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), null));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleError(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(e.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleError(MethodArgumentNotValidException e) {

        Map<String, String> reasons = new HashMap<>();
        e.getBindingResult().getAllErrors().parallelStream().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            reasons.put(fieldName, fieldName + " " + error.getDefaultMessage());
        });

        return ResponseEntity.unprocessableEntity().body(new ErrorResponse(ErrorMessage.UNPROCESSABLE_ENTITY, reasons));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleError(HttpMessageNotReadableException e) {
        String errorMessage = e.getMessage();
        logger.error(errorMessage, e);

        String[] split = errorMessage != null ? errorMessage.split(":") : null;

        return ResponseEntity.badRequest().body(new ErrorResponse((split == null || split.length == 0) ? errorMessage : split[0]));
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        HttpStatus httpStatus = getHttpStatus(request, true);
        ErrorResponse errorResponse = getErrorResponse(request, httpStatus);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public static HttpStatus getHttpStatus(HttpServletRequest request, boolean defaultTo500) {

        String code = request.getParameter("code");
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        HttpStatus httpStatus;

        if (status != null) httpStatus = HttpStatus.valueOf(status);
        else if (code != null && code.length() > 0) httpStatus = HttpStatus.valueOf(code);
        else httpStatus = defaultTo500 ? HttpStatus.INTERNAL_SERVER_ERROR : null;

        return httpStatus;
    }

    private ErrorResponse getErrorResponse(HttpServletRequest request, HttpStatus httpStatus) {

        ErrorResponse errorResponse;

        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        switch (httpStatus) {
            case NOT_FOUND:
                errorResponse = new ErrorResponse(ErrorMessage.NOT_FOUND);
                break;
            case INTERNAL_SERVER_ERROR:
                errorResponse = new ErrorResponse(ErrorMessage.INTERNAL_SERVER_ERROR);
                break;
            case FORBIDDEN:
                errorResponse = new ErrorResponse(ErrorMessage.FORBIDDEN);
                break;
            case UNAUTHORIZED:
                errorResponse = new ErrorResponse(ErrorMessage.UNAUTHORIZED);
                break;
            default:
                errorResponse = new ErrorResponse((message != null && !message.isEmpty()) ? message : httpStatus.getReasonPhrase());
        }

        return errorResponse;
    }
}