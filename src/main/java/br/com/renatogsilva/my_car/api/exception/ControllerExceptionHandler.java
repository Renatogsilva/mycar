package br.com.renatogsilva.my_car.api.exception;

import br.com.renatogsilva.my_car.model.enumerators.EnumMessageGenericExceptions;
import br.com.renatogsilva.my_car.model.exceptions.car.CarDuplicationException;
import br.com.renatogsilva.my_car.model.exceptions.car.CarNotFoundException;
import br.com.renatogsilva.my_car.model.exceptions.person.PersonDuplicationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserAuthenticationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserDuplicationException;
import br.com.renatogsilva.my_car.model.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CarDuplicationException.class)
    public ResponseEntity<Object> handleCarDuplicationException(final CarDuplicationException ex, final WebRequest request) {
        StandartError error = new StandartError(ex.getMessage(), ex.getCode(), LocalDateTime.now());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Object> handleCarNotFoundException(final CarNotFoundException ex, final WebRequest request) {
        StandartError error = new StandartError(ex.getMessage(), ex.getCode(), LocalDateTime.now());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserDuplicationException.class)
    public ResponseEntity<Object> handleUserDuplicationException(final UserDuplicationException ex, final WebRequest request) {
        StandartError error = new StandartError(ex.getMessage(), ex.getCode(), LocalDateTime.now());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserrNotFoundException(final UserNotFoundException ex, final WebRequest request) {
        StandartError error = new StandartError(ex.getMessage(), ex.getCode(), LocalDateTime.now());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<Object> handlePersonDuplicationException(final UserAuthenticationException ex, final WebRequest request) {
        StandartError error = new StandartError(ex.getMessage(), ex.getCode(), LocalDateTime.now());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
    
    @ExceptionHandler(PersonDuplicationException.class)
    public ResponseEntity<Object> handlePersonDuplicationException(final PersonDuplicationException ex, final WebRequest request) {
        StandartError error = new StandartError(ex.getMessage(), ex.getCode(), LocalDateTime.now());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex,
                                                                      final WebRequest request) {
        StandartError error = new StandartError(EnumMessageGenericExceptions.INVALID_PARAMETER.getMessage(),
                EnumMessageGenericExceptions.INVALID_PARAMETER.getCode(),
                LocalDateTime.now());
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        StandartError error = new StandartError(message, status.value(), LocalDateTime.now());

        return handleExceptionInternal(ex, error, headers, status, request);
    }
}