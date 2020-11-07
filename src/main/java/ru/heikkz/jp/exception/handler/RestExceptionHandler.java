package ru.heikkz.jp.exception.handler;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.heikkz.jp.exception.*;
import ru.heikkz.jp.exception.dto.ValidationErrorDTO;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // 400
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info("Bad Request: " + ex.getMessage());
        logger.debug("Bad Request: ", ex);

        ApiError apiError = message(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info("Bad Request: " + ex.getMessage());
        logger.debug("Bad Request: ", ex);

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ValidationErrorDTO dto = processFieldErrors(fieldErrors);
        return handleExceptionInternal(ex, dto, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class, MyBadRequestException.class, UsernameNotFoundException.class,
            BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        logger.info("Bad Request: " + ex.getMessage());
        logger.debug("Bad Request: ", ex);

        ApiError apiError = message(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 403
    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<Object> handleEverything(AccessDeniedException ex, WebRequest request) {
        logger.error("403 Status Code", ex);

        ApiError apiError = message(HttpStatus.FORBIDDEN, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    // 404
    @ExceptionHandler(value = {EntityNotFoundException.class, MyEntityNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        logger.warn("Not Found: " + ex.getMessage());

        ApiError apiError = message(HttpStatus.NOT_FOUND, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409
    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class})
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        logger.warn("Conflict: " + ex.getMessage());

        ApiError apiError = message(HttpStatus.CONFLICT, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // 415
    @ExceptionHandler({ InvalidMimeTypeException.class, InvalidMediaTypeException.class })
    protected ResponseEntity<Object> handleInvalidMimeTypeException(final IllegalArgumentException ex, final WebRequest request) {
        logger.warn("Unsupported Media Type: " + ex.getMessage());

        ApiError apiError = message(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class, CoreException.class})
    protected ResponseEntity<Object> handle500s(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);

        ApiError apiError = message(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // UTIL
    private ValidationErrorDTO processFieldErrors(List<FieldError> errors) {
        ValidationErrorDTO dto= new ValidationErrorDTO();
        for (FieldError error : errors) {
            String localizedErrorMessage = error.getDefaultMessage();
            dto.addFieldError(error.getField(), localizedErrorMessage);
        }
        return dto;
    }

    private ApiError message(HttpStatus httpStatus, Exception ex) {
        String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        String devMessage = ex.getClass().getSimpleName();
        // devMessage = ExceptionUtils.getStackTrace(ex);
        return new ApiError(httpStatus.value(), message, devMessage);
    }
}
