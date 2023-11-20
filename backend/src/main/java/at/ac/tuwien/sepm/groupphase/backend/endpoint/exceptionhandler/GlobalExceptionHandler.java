package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepm.groupphase.backend.exception.*;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Register all your Java exceptions here to map them into meaningful HTTP exceptions If you have
 * special cases which are only important for specific endpoints, use ResponseStatusExceptions
 * https://www.baeldung.com/exception-handling-for-rest-with-spring#responsestatusexception
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleImageTooBig(MultipartException e, WebRequest request) {
        LOGGER.error(e.getMessage());
        return handleExceptionInternal(e, "One of your files you provided is to big! (<2MB)",
            new HttpHeaders(), HttpStatus.BAD_REQUEST,
            request);
    }

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions.
     */
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND,
            request);
    }

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions.
     */
    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidation(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
            HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions.
     */
    @ExceptionHandler(value = {ValidHallplanExeption.class})
    protected ResponseEntity<Object> handleValidHallplan(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
            HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions.
     */
    @ExceptionHandler(value = {javax.validation.ValidationException.class})
    protected ResponseEntity<Object> handleValid(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
            HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    protected ResponseEntity<Object> handleEmailAlreadyTaken(RuntimeException ex, WebRequest request) {
        LOGGER.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * Override methods from ResponseEntityExceptionHandler to send a customized HTTP response for a
     * know exception from e.g. Spring
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        LOGGER.error(ex.toString());

        //Get all errors
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.toList());

        StringBuilder stringBuilder = new StringBuilder();
        boolean before = false;
        for (String s : errors) {
            if (before) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(s);
            before = true;
        }

        return new ResponseEntity<>(stringBuilder.toString(), headers, status);
    }

    @ExceptionHandler(value = {ConflictException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
            HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {javax.persistence.RollbackException.class})
    protected ResponseEntity<Object> handleRollback(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        ex.printStackTrace();
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
            HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {SelfLockException.class})
    protected ResponseEntity<Object> handleSelfLock(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
            HttpStatus.CONFLICT, request);
    }
}
