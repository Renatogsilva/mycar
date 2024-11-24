package br.com.renatogsilva.my_car.model.exceptions.user;

import br.com.renatogsilva.my_car.model.exceptions.basic.ObjectDuplicationException;
import lombok.Data;

@Data
public class UserAuthenticationException extends ObjectDuplicationException {
    private int code;

    public UserAuthenticationException(String message) {
        super(message);
    }

    public UserAuthenticationException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
