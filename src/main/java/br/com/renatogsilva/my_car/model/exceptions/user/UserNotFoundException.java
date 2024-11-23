package br.com.renatogsilva.my_car.model.exceptions.user;

import br.com.renatogsilva.my_car.model.exceptions.basic.ObjectNotFoundException;
import lombok.Getter;

@Getter
public class UserNotFoundException extends ObjectNotFoundException {
    private int code;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
