package br.com.renatogsilva.my_car.api.exception;

import br.com.renatogsilva.my_car.model.enumerators.EnumMessageExceptions;
import br.com.renatogsilva.my_car.model.exceptions.ObjectDuplicationException;
import br.com.renatogsilva.my_car.model.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandartError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

        return ResponseEntity.status(e.getCode()).body(getInstance(e));
    }

    @ExceptionHandler(ObjectDuplicationException.class)
    public ResponseEntity<StandartError> objectDuplication(ObjectDuplicationException e, HttpServletRequest request) {
        StandartError error = new StandartError(e.getMessage(), e.getCode(), System.currentTimeMillis());

        return ResponseEntity.status(e.getCode()).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandartError> argumentTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        StandartError error = new StandartError(EnumMessageExceptions.INVALID_PARAMETER.getMessage(),
                EnumMessageExceptions.INVALID_PARAMETER.getCode(), System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    private StandartError getInstance(ObjectNotFoundException e) {
        return new StandartError(e.getMessage(), e.getCode(), System.currentTimeMillis());
    }
}
